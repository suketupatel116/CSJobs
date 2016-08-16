package csjobs.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import csjobs.model.Application;
import csjobs.model.Job;
import csjobs.model.User;
import csjobs.model.dao.JobDao;
import csjobs.model.dao.UserDao;
import csjobs.security.SecurityUtils;
import csjobs.web.validator.UserValidator;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private UserValidator userValidator;

    private static final Logger logger = LoggerFactory
        .getLogger( UserController.class );

    @RequestMapping(value = "/register.html", method = RequestMethod.GET)
    public String register( ModelMap models )
    {
        models.put( "user", new User() );
        return "register";
    }

    @RequestMapping(value = "/register.html", method = RequestMethod.POST)
    public String register( @ModelAttribute User user,
        BindingResult bindingResult, SessionStatus sessionStatus )
    {
        userValidator.validate( user, bindingResult );
        if( bindingResult.hasErrors() ) return "register";

        user = userDao.saveUser( user );
        sessionStatus.setComplete();

        logger.info( "Registered new user " + user.getEmail() );

        return "redirect:/";
    }

    /*@RequestMapping("/login.html")
    public String login()
    {
        return "login";
    }
*/
    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public String login() {
		System.out.println("Location login GET");
		return "login";
	}

	@RequestMapping(value = "/logincsrf.html", method = RequestMethod.GET)
	public String login(HttpSession session) {
		/*
		 * User user = userDao.getUser( email ); if( user == null ||
		 * !user.getPassword().equals( password ) ) return
		 * "redirect:/login.html";
		 */
		
		String email = SecurityUtils.getUser();
		User user = userDao.getUser(email);

		logger.info(user.getEmail() + " logged in.");

		session.setAttribute("authenticatedUser", user);
		if (user.isAdmin())
			return "redirect:/admin.html";
		else if (user.isReviewer())
			return "redirect:/reviewer/reviewer.html";
		else
			return "redirect:/applicant.html";
	}

    
    @RequestMapping("/admin.html")
    public String admin( ModelMap models )
    {
        models.put( "jobs", jobDao.getJobs() );
        return "admin";
    }

    @RequestMapping("/reviewer/reviewer.html")
    public String reviewer(HttpSession session, ModelMap models)
    {
    	User authenticatedUser = (User) session
				.getAttribute("authenticatedUser");
		Long temp = authenticatedUser.getId();
		User user = userDao.getUser(temp);
		// System.out.println(authenticatedUser.getEmail());
		List<Job> jobs = jobDao.getUnPublishedJobs();
		List<Job> reviewerJobs = new ArrayList<Job>();
		
		for (Job j : jobs) {
			for (User u : j.getCommitteeMembers()) {
				if (!reviewerJobs.contains(j) && u.equals(user)) {
					reviewerJobs.add(j);
				}
			}
		}
		models.put("jobs", jobs);
		models.put("authenticatedUser", authenticatedUser);
		models.put("reviewerJobs", reviewerJobs);
		return "reviewer/reviewer";
        //return "reviewer";
    }

    @RequestMapping("/applicant.html")
    public String applicant( ModelMap models )
    {
        // The User object in the session is a "detached" object, which means
        // it may not be update-to-date, and it cannot be used to load other
        // objects. So here we just use the id to retrieve another copy of the
        // user from the database.
    	String email = SecurityUtils.getUser();
		User user = userDao.getUser(email);
//        User user = userDao.getUser( SecurityUtils.getUser() );

        Set<Job> appliedJobs = new HashSet<Job>();
        for( Application application : user.getApplications() )
            appliedJobs.add( application.getJob() );

        List<Job> availableJobs = jobDao.getOpenJobs();
        Iterator<Job> iterator = availableJobs.iterator();
        while( iterator.hasNext() )
            if( appliedJobs.contains( iterator.next() ) ) iterator.remove();

        models.put( "user", user );
        models.put( "availableJobs", availableJobs );

        return "applicant";
    }
	
}
