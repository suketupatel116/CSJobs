package csjobs.model.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csjobs.model.Job;
import csjobs.model.Review;
import csjobs.model.Round;
import csjobs.model.User;
import csjobs.model.dao.JobDao;

@Repository
public class JobDaoImpl implements JobDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @PostAuthorize("returnObject.published or hasRole('ROLE_ADMIN')")
    public Job getJob( Long id )
    {
        return entityManager.find( Job.class, id );
    }

    @Override
    public List<Job> getJobs()
    {
        return entityManager
            .createQuery( "from Job order by id desc", Job.class )
            .getResultList();
    }

    @Override
    public List<Job> getOpenJobs()
    {
        String query = "from Job where publishDate < :now "
            + "and (closeDate is null or closeDate > :now) "
            + "order by publishDate asc";

        return entityManager.createQuery( query, Job.class )
            .setParameter( "now", new Date() )
            .getResultList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Job saveJob( Job job )
    {
        return entityManager.merge( job );
    }

    @Override
    public List<Job> searchJobs(String title, int maxResults)
    {

        TypedQuery<Job> query = entityManager.createNamedQuery(
                "job.search", Job.class );
            if( maxResults > 0 ) query.setMaxResults( maxResults );
            return query.setParameter( "title", title ).getResultList();
    }
    
    @Override
    public List<Job> getUnPublishedJobs()
    {
        String query = "from Job where publishDate < :now "
            + "order by publishDate asc";

        return entityManager.createQuery( query, Job.class )
            .setParameter( "now", new Date() )
            .getResultList();
    }

    @Override
    @Transactional
    public Review saveReview( Review review )
    {
        return entityManager.merge( review );
    }
    
    @Override
    @Transactional
    public Round saveRound( Round round )
    {
        return entityManager.merge( round );
    }

}
