package csjobs.model.dao;

import java.util.List;

import csjobs.model.Job;
import csjobs.model.Review;
import csjobs.model.Round;
import csjobs.model.User;

public interface JobDao {

    Job getJob( Long id );

    List<Job> getJobs();

    List<Job> getOpenJobs();

    Job saveJob( Job job );

	List<Job> searchJobs(String title, int maxResults);

	List<Job> getUnPublishedJobs();

	Review saveReview(Review review);

	Round saveRound(Round round);

}
