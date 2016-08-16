package csjobs.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csjobs.model.Application;
import csjobs.model.Job;
import csjobs.model.Review;
import csjobs.model.Round;
import csjobs.model.User;
import csjobs.model.dao.ApplicationDao;
import csjobs.model.dao.JobDao;

@Controller
public class ApplicationController {

	@Autowired
	private ApplicationDao applicationDao;

	@Autowired
	private JobDao jobDao;

	@RequestMapping("/application/view.html")
	public String view(@RequestParam Long id, ModelMap models) {
		models.put("application", applicationDao.getApplication(id));
		return "application/view";
	}

	@RequestMapping("/reviewer/view.html")
	public String view(HttpSession session, ModelMap models,
			HttpServletResponse response, @RequestParam Long id,
			@RequestParam(required = false) Integer roundId) {
		// Long id = (Long)session.getAttribute("id");
		System.out.println(id);

		Application application = new Application();
		application = applicationDao.getApplication(id);

		models.put("roundId", roundId);
		models.put("application", application);
		return "reviewer/view";
	}

	@RequestMapping("/reviewer/chairview.html")
	public String chairview(HttpSession session, ModelMap models,
			HttpServletResponse response, @RequestParam Long id) {

		Application application = new Application();
		application = applicationDao.getApplication(id);

		Job job = jobDao.getJob(id);
		System.out.println(job.getApplications());
		List<Application> applications = new ArrayList<Application>();

		for (Application a : job.getApplications()) {

			applications.add(a);
		}

		models.put("applications", applications);

		models.put("application", application);
		return "reviewer/chairview";

	}

	@RequestMapping("/reviewer/chairviewr2.html")
	public String chairviewr2(HttpSession session, ModelMap models,
			HttpServletResponse response, @RequestParam Long id,
			@RequestParam Integer roundId) {
		System.out.println(roundId);

		Application application = new Application();
		application = applicationDao.getApplication(id);

		Job job = jobDao.getJob(id);
		System.out.println(job.getApplications());
		List<Application> applications = new ArrayList<Application>();

		for (Application a : job.getApplications()) {
			for (Round r : a.getRounds()) {

				if (r.getIndex() == roundId - 1 && r.isPassed()) {
					System.out.println("Location r2");
					applications.add(a);
				}

			}

		}

		models.put("applications", applications);

		models.put("application", application);
		return "reviewer/chairviewr2";

	}

	@RequestMapping("/reviewer/chairviewr3.html")
	public String chairviewr3(HttpSession session, ModelMap models,
			HttpServletResponse response, @RequestParam Long id,
			@RequestParam Integer roundId) {
		System.out.println("id" + id);

		List<User> reviewers = new ArrayList<User>();
		List<Integer> t = new ArrayList<Integer>();
		List<Integer> finalranking = new ArrayList<>();

		Job job = jobDao.getJob(id);
		System.out.println(job.getApplications());
		List<Application> applications = new ArrayList<Application>();

		for (Application a : job.getApplications()) {
			for (Round r : a.getRounds()) {

				if (r.getIndex() == 1 && r.isPassed()) {
					//System.out.println("Location r2");
					applications.add(a);
				}
			}
		}

		reviewers = job.getCommitteeMembers();

		for (Application a : applications) {
			for (User u : reviewers) {
				for (Round r : a.getRounds()) {
					for (Review w : r.getReviews()) {
						if (w.getReviewer().getId() == u.getId()
								&& w.getRank() != null) {
//							/System.out.println("ranks: " + w.getRank());
							t.add(w.getRank());
						}
					}

				}

			}

		}
		int b = 2;

		for (Application a : applications) {
			for(Round r : a.getRounds()){
				if(r.getIndex() == 2){
					for(Review w : r.getReviews()){
						System.out.println("rank: "+w.getRank());
					}
				}
			}
		}

		Integer[] total = new Integer[applications.size()];

		for(int i = 0; i<applications.size(); i++){
			total[i] = 0;
			for(int j = 0; j<reviewers.size(); j++){
				total[i] = total[i]+t.get(i*reviewers.size()+j);
				
			}
			//System.out.println(total[i]);
		}
		
		List<Integer> ttotal = new ArrayList<Integer>();
		List<Integer> finalttotal = new ArrayList<Integer>();
		
		for(Integer l : total){
			ttotal.add(l);
			System.out.println(ttotal);
		}
		models.put("ttotal", ttotal);
		
		for(Integer k:ttotal)
		{
			finalranking.add(k);
		}
		
		Collections.sort(finalranking);
		List<Integer> tfinalranking = new ArrayList<>();
		/*List<Integer> finalranking = new ArrayList<>();
		Collections.sort(ttotal);
		finalranking = ttotal;*/
		//
		
/*		for(int i = 0; i<ttotal.size(); i++){
			for(int j = 0; j<=i; j++){
				if(ttotal.get(i)==finalranking.get(j)){
					System.out.println("hi");
				}
			}
			System.out.println("Hello");
		}*/
		//ttotal.indexOf(o//

		boolean[] abc = new boolean[applications.size()];
		
		for(int i = 0 ; i< applications.size(); i++ ){
			abc[i]=false;
		}
		
 		
		for(int i=0; i<finalranking.size(); i++){
			for(int j=0 ; j<ttotal.size(); j++){
				if(i==j && !abc[j]){
					tfinalranking.add(j+1);
					abc[j] = true;
					break;
				}
				
			}
		}

		models.put("tfinalranking", tfinalranking);
		models.put("finalranking", finalranking);
		models.put("applications", applications);
		models.put("reviewers", reviewers);
		return "reviewer/chairviewr3";

	}

}
