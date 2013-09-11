/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile;

/**
 *
 * @author programador
 */
public class FilesConfig {

    private String repository = System.getProperty("user.home") + "/files";
    private Integer maxSize = 1024;
    private Integer maxThumbnail = 100;

    public FilesConfig() {
    }

    public FilesConfig(String repo, Integer maxSize) {
        this.repository = repo;
        this.maxSize = maxSize;
    }

    public FilesConfig(String repo, Integer maxSize, Integer maxThumbnail) {
        this.repository = repo;
        this.maxSize = maxSize;
        this.maxThumbnail = maxThumbnail;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getMaxThumbnail() {
        return maxThumbnail;
    }

    public void setMaxThumbnail(Integer maxThumbnail) {
        this.maxThumbnail = maxThumbnail;
    }

    public FilesConfig(String repo) {
        this.repository = repo;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repositorio) {
        this.repository = repositorio;
    }
}
