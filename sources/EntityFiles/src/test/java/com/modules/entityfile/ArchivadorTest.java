package com.modules.entityfile;

import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.modules.entityfile.FilesConfig;
import com.dynamia.modules.entityfile.service.EntityFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for simple App.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ArchivadorTest {

    @Autowired
    private EntityFileService archivadorService;
    @Autowired
    private CrudService crudService;
    @Autowired
    private FilesConfig  filesConfig;

    @Test
    //@Transactional
    public void testRegistro() {
        /*System.out.println(""+filesConfig.getRepositorio());
        BaseEntity baseEntity=new  Directory();
        baseEntity.setId(123l);
        Directory dir1=archivadorService.registerDir(baseEntity, "nuevo","SYSTEM");
        
        System.out.println(">:"+dir1.getId());
        System.out.println(">:"+dir1.getClassFileable());
        System.out.println(">:"+dir1.getIdFileable());
        System.out.println(">:"+dir1.getName());
        System.out.println(">:"+dir1.getType());
        if(dir1.getDirectoryPadre()!=null){
            System.out.println(">>:"+dir1.getDirectoryPadre().getId());
        }

        Directory dir2=archivadorService.registerDir(dir1, "segundo","SYSTEM");

        System.out.println(">:"+dir2.getId());
        System.out.println(">:"+dir2.getClassFileable());
        System.out.println(">:"+dir2.getIdFileable());
        System.out.println(">:"+dir2.getName());
        System.out.println(">:"+dir2.getType());
        if(dir2.getDirectoryPadre()!=null){
            System.out.println(">>:"+dir2.getDirectoryPadre().getId());
        }

        java.io.File f=new java.io.File("/home/programador/Descargas/teamviewer_linux_x64.deb");
        File fileX=archivadorService.registerFile(dir2, f,"SYSTEM");

        System.out.println(">:"+fileX.getId());
        System.out.println(">:"+fileX.getClassFileable());
        System.out.println(">:"+fileX.getIdFileable());
        System.out.println(">:"+fileX.getName());
        System.out.println(">:"+fileX.getType());
        System.out.println(">:"+fileX.getSizeFile());

        if(fileX.getDirectoryPadre()!=null){
            System.out.println(">>:"+fileX.getDirectoryPadre().getId());
        }

        List<GenericFile> lista=archivadorService.loadFiles(dir1);
        for (GenericFile genericFile : lista) {
            if(genericFile instanceof File){
                System.out.println("File:"+genericFile.getId());
                System.out.println(""+((File)genericFile).getContent().getName());
                System.out.println(""+((File)genericFile).getContent().length());
            }else{
                System.out.println("Dir:"+genericFile.getId());
                List<GenericFile> t=((Directory)genericFile).getChildrens();
                for (GenericFile genericFile1 : t) {
                    System.out.println("M+:"+genericFile1.getId());
                }
            }
        }
        assert("".equals(""));*/
    }

}
