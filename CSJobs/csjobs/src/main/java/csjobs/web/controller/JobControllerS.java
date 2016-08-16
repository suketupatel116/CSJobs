package csjobs.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;

import csjobs.model.Application;
import csjobs.model.Job;
import csjobs.model.Review;
import csjobs.model.Round;
import csjobs.model.User;
import csjobs.model.dao.ApplicationDao;
import csjobs.model.dao.JobDao;
import csjobs.model.dao.UserDao;
import csjobs.web.editor.UserPropertyEditor;

@Controller
@SessionAttributes("job")
public class JobControllerS {

	@Autowired
	private UserDao userDao;

	@Autowired
	private JobDao jobDao;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private ApplicationDao applicationDao;

	@Autowired
	private WebApplicationContext context;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(User.class,
				(UserPropertyEditor) context.getBean("userPropertyEditor"));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("M/d/yyyy"), true));
	}

	@RequestMapping(value = "/job/create.html", method = RequestMethod.GET)
	public String create(ModelMap models) {
		models.put("job", new Job());
		models.put("reviewers", userDao.getUsers("ROLE_REVIEWER"));
		return "job/create";
	}

	@RequestMapping(value = "/job/create.html", method = RequestMethod.POST)
	public String create(@ModelAttribute Job job, SessionStatus sessionStatus) {

		job = jobDao.saveJob(job);
		if (!job.getCommitteeMembers().contains(job.getCommitteeChair())) {
			job.getCommitteeMembers().add(job.getCommitteeChair());
		}
		job = jobDao.saveJob(job);
		sessionStatus.setComplete();

		return "redirect:/admin.html";
	}

	@RequestMapping(value = "/job/edit.html", method = RequestMethod.GET)
	public String edit(@RequestParam Long id, ModelMap models) {
		models.put("job", jobDao.getJob(id));
		models.put("reviewers", userDao.getUsers("ROLE_REVIEWER"));
		return "job/edit";
	}

	@RequestMapping(value = "/job/edit.html", method = RequestMethod.POST)
	public String edit(@ModelAttribute Job job, SessionStatus sessionStatus) {
		job = jobDao.saveJob(job);
		sessionStatus.setComplete();

		return "redirect:/admin.html";
	}

	@RequestMapping(value = "/reviewer/addcomment.html", method = RequestMethod.POST)
	public String addcomment(@RequestParam String content, HttpSession session,
			@RequestParam Long id, @RequestParam Integer roundId) {
		System.out.println("Location addcomment post");
		System.out.println(content);
		System.out.println("Hey id here:" + id);
		System.out.println("round id: " + roundId);
		User authenticatedUser = (User) session
				.getAttribute("authenticatedUser");
		
		Application application = applicationDao.getApplication(id);
		List<Round> round = application.getRounds();
		User reviewer = (User) session.getAttribute("authenticatedUser");
		Review review = new Review();
		
		System.out.println();
		System.out.println();

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(authenticatedUser.getEmail());
		msg.setTo(application.getApplicant().getEmail());
		msg.setSubject("Comment round: "+roundId+1);
		msg.setText(content);
		mailSender.send(msg);
		
		for (Round r : round) {

			System.out.println("Index:-->" + r.getIndex());
			if (r.getIndex() == roundId) {
				review.setRound(r);
			}
		}

		review.setDate(new Date());
		review.setComments(content);
		review.setReviewer(reviewer);
		// review.setRound(round);
		review = jobDao.saveReview(review);

		return "redirect:/reviewer/reviewer.html";
	}

	@RequestMapping("/reviewer/viewcomments.html")
	public String viewcomments(@RequestParam Long id, ModelMap models,
			@RequestParam Integer roundId) {
	
		System.out.println(id);
		
		Application application = applicationDao.getApplication(id);
		System.out.println(application.getJob().getTitle());
		System.out.println(roundId);
		List<Round> rounds = application.getRounds();
		Round round = new Round();
		
		Review review = new Review();
		List<String> comments = new ArrayList<String>(); 
		
		for (Round r : rounds) {

			System.out.println("Index:-->" + r.getIndex());
			if (r.getIndex() == roundId) {
				//System.out.println("round: "+ r.getId());
				round = r;
			}
		}
		System.out.println(round.getId());
	
		List<Review> reviews = new ArrayList<Review>();
		System.out.println("on reviews list");
		for(Review r : round.getReviews()){
			reviews.add(r);
			System.out.println("comment: "+r.getComments());
			comments.add(r.getComments());
			
		}
		 models.put( "application", application ); 
		 models.put( "reviews", reviews ); 
		 models.put( "comments", comments ); 
		 models.put( "id", id ); 
		 models.put( "roundId", roundId ); 
		 
		 models.put( "reviewers", userDao.getUsers( "ROLE_REVIEWER" ) );
		 
		return "reviewer/viewcomments";
	}

	@RequestMapping(value = "/reviewer/tonextround.html", method = RequestMethod.POST)
	public String yes(HttpSession session,
			@RequestParam Long id, @RequestParam Integer roundId) {
		System.out.println("Location addcomment post");
		System.out.println("Hey id here:" + id);
		System.out.println("round id: " + roundId);

		Application application = applicationDao.getApplication(id);
		
		Round round = new Round();
		
		List<Round> rounds = application.getRounds();
		
		for(Round r : rounds){
			
			if(r.getIndex() == roundId){
				round = r;
			}
		}
	
		round.setPassed(true);
		
		jobDao.saveRound(round);
		
		return "redirect:/reviewer/reviewer.html";
	}
	
	@RequestMapping(value = "/reviewer/no.html", method = RequestMethod.POST)
	public String no(HttpSession session) {
		System.out.println("location no post");
		return "redirect:/reviewer/reviewer.html";
		
	}
	
	@RequestMapping(value = "/reviewer/addrank.html", method = RequestMethod.POST)
	public String addrank(HttpSession session,
			@RequestParam Long id, @RequestParam Integer roundId) {
		
		return "redirect:/reviewer/addrank.html";
	}

}
