package csjobs.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import csjobs.model.Application;
import csjobs.model.Job;
import csjobs.model.Review;
import csjobs.model.Round;
import csjobs.model.User;
import csjobs.model.dao.ApplicationDao;
import csjobs.model.dao.JobDao;
import csjobs.model.dao.UserDao;

@Controller
public class JobController {

	@Autowired
	private JobDao jobDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ApplicationDao applicationDao;

	@RequestMapping({ "/index.html", "/job/list.html" })
	public String list(ModelMap models) {
		models.put("openJobs", jobDao.getOpenJobs());
		return "job/list";
	}

	@RequestMapping("/job/view.html")
	public String view(@RequestParam Long id, ModelMap models) {
		models.put("job", jobDao.getJob(id));
		return "job/view";
	}

	@RequestMapping("/job/search.html")
	public String search(@RequestParam(required = false) String sbox,
			ModelMap models) {
		if (StringUtils.hasText(sbox))
			models.addAttribute("jobs", jobDao.searchJobs(sbox, -1));

		return "job/search";
	}

	@RequestMapping("/reviewer/applications.html")
	public String applications(HttpSession session, ModelMap models,
			@RequestParam Long id, @RequestParam Integer roundId) {
		User authenticatedUser = (User) session
				.getAttribute("authenticatedUser");
		Job job = jobDao.getJob(id);
		// System.out.println(job.getApplications());
		System.out.println("Location ");
		System.out.println(roundId);
		List<Application> applications = new ArrayList<Application>();

		for (Application a : job.getApplications()) {

			applications.add(a);
		}

		models.put("roundId", roundId);
		models.put("applications", applications);

		return "reviewer/applications";
	}

	@RequestMapping("/reviewer/applicationsr2.html")
	public String applicationsr2(HttpSession session, ModelMap models,
			@RequestParam Long id, @RequestParam Integer roundId) {
		User authenticatedUser = (User) session
				.getAttribute("authenticatedUser");
		Job job = jobDao.getJob(id);
		// System.out.println(job.getApplications());
		// System.out.println("Location ");
		// System.out.println(roundId);
		List<Application> applications = new ArrayList<Application>();

		for (Application a : job.getApplications()) {

			for (Round r : a.getRounds()) {

				if (r.getIndex() == roundId - 1 && r.isPassed()) {
					// System.out.println("Location r2");
					applications.add(a);
				}

			}
		}

		models.put("roundId", roundId);
		models.put("applications", applications);

		return "reviewer/applicationsr2";
	}

	@RequestMapping(value = "/reviewer/applicationsr3.html", method = RequestMethod.GET)
	public String applicationsr3(HttpSession session, ModelMap models,
			@RequestParam Long id, @RequestParam Integer roundId) {
		User authenticatedUser = (User) session
				.getAttribute("authenticatedUser");
		Job job = jobDao.getJob(id);
		// System.out.println(job.getApplications());
		// System.out.println("Location ");
		// System.out.println(roundId);
		List<Application> applications = new ArrayList<Application>();
		List<Application> applications2 = new ArrayList<Application>();
		List<Integer> subdropdown = new ArrayList<Integer>();

		for (Application a : job.getApplications()) {

			for (Round r : a.getRounds()) {

				if (r.getIndex() == roundId - 1 && r.isPassed()) {

					for (Review w : r.getReviews()) {
						// System.out.println(w.getId());

						// System.out.println("Location r2 on here");
						if (w.getRank() != null
								&& w.getReviewer().getEmail()
										.equals(authenticatedUser.getEmail())) {
							applications2.add(a);
							if (!subdropdown.contains(w.getRank())) {
								subdropdown.add(w.getRank());
							}
						} else{
							System.out.println(a.getCurrentJobTitle());
							if(!applications.contains(a)){
							applications.add(a);
					}}}
				}
			}
		}

		applications.removeAll(applications2);
		int size = applications.size() + applications2.size();
		System.out.println(applications.size());
		List<Integer> dropdown = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			dropdown.add(i + 1);
			/*
			 * int j = i+1; System.out.println("size:"+j);
			 */
		}

		dropdown.removeAll(subdropdown);

		for (Integer i : dropdown) {
			System.out.println("size:" + i);
		}

		models.put("id", id);
		models.put("dropdown", dropdown);
		models.put("roundId", roundId);
		models.put("applications", applications);
		models.put("applications2", applications2);

		return "reviewer/applicationsr3";
	}

	@RequestMapping(value = "/reviewer/applicationsr3.html", method = RequestMethod.POST)
	public String applicationsr3a(HttpSession session, ModelMap models,
			@RequestParam Long id, @RequestParam Integer roundId,
			@RequestParam Integer rank, @RequestParam Long appId,
			@RequestParam Long applicantId) {
		/*
		 * System.out.println("app r3 post"); System.out.println(rank);
		 * System.out.println(appId); System.out.println(id);
		 * System.out.println(applicantId);
		 */
		User user2 = (User) session.getAttribute("authenticatedUser");

		User user = userDao.getUser(applicantId);
		System.out.println(user.getEmail());
		Review review = new Review();
		Job job = jobDao.getJob(id);
		System.out.println(job.getTitle());
		List<Application> applications = job.getApplications();
		Application application = new Application();

		for (Application a : applications) {
			// System.out.println(a.getId());
			// System.out.println(appId);
			if (appId.equals(a.getId())) {
				// System.out.println("Location");
				application = a;
			}

		}
		// System.out.println(application.getJob().getTitle());
		Round round = new Round();

		for (Round r : application.getRounds()) {

			if (r.getIndex() == roundId - 1) {
				// System.out.println("Location r2");
				round = r;

			}

		}

		review.setRank(rank);
		review.setRound(round);
		review.setReviewer(user2);
		review.setDate(new Date());

		review = jobDao.saveReview(review);

		return "redirect:/reviewer/applicationsr3.html?id=" + id + "&roundId="
				+ roundId;
	}
}
