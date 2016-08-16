package csjobs.util;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import csjobs.model.File;
import csjobs.model.User;
import csjobs.model.dao.FileDao;

@Component
public class FileIO {

    @Autowired
    FileDao fileDao;

    private static final Logger logger = LoggerFactory
        .getLogger( FileIO.class );

    public File save( String fileDir, MultipartFile uploadedFile, User user )
    {
        if( uploadedFile.isEmpty() ) return null;

        File file = new File();
        file.setName( uploadedFile.getOriginalFilename() );
        file.setType( uploadedFile.getContentType() );
        file.setSize( uploadedFile.getSize() );
        file.setDate( new Date() );
        file.setOwner( user );
        file = fileDao.saveFile( file );

        java.io.File diskFile = new java.io.File( fileDir,
            file.getId().toString() );
        try
        {
            uploadedFile.transferTo( diskFile );
        }
        catch( Exception e )
        {
            logger.error( "Failed to save uploaded file", e );
        }

        return file;
    }

    public void write( String fileDir, File file, HttpServletResponse response )
    {
        try
        {
            response.setContentType( file.getType() );
            response.setHeader( "Content-Disposition",
                "inline; filename=" + file.getName() );
            FileInputStream in = new FileInputStream(
                new java.io.File( fileDir, file.getId().toString() ) );
            OutputStream out = response.getOutputStream();

            byte buffer[] = new byte[2048];
            int bytesRead;
            while( (bytesRead = in.read( buffer )) > 0 )
                out.write( buffer, 0, bytesRead );

            in.close();
        }
        catch( Exception e )
        {
            logger.error( "Fail to write file to response", e );
        }
    }

}
