package tools.dynamia.modules.entityfile;

/*-
 * #%L
 * Dynamia Modules - EntityFiles - Core
 * %%
 * Copyright (C) 2016 - 2019 Dynamia Soluciones IT SAS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;

public class UploadedFileInfo {

    private String fullName;
    private String contentType;
    private InputStream inputStream;
    private EntityFile parent;
    private boolean shared;
    private String subfolder;
    private String storedFileName;
    private Long accountId;

    public UploadedFileInfo() {
        //default
    }

    public UploadedFileInfo(File file) throws FileNotFoundException {
        this(new FileInfo(file));
    }

    public UploadedFileInfo(FileInfo info) throws FileNotFoundException {
        this.fullName = info.getName();
        this.inputStream = new FileInputStream(info.getFile());
    }

    public UploadedFileInfo(Path file) throws IOException {
        this.fullName = file.getFileName().toString();
        this.inputStream = Files.newInputStream(file);
    }

    public UploadedFileInfo(String fullName, InputStream inputStream) {
        super();
        this.fullName = fullName;
        this.inputStream = inputStream;
    }

    public UploadedFileInfo(String fullName, String contentType, InputStream inputStream) {
        super();
        this.fullName = fullName;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public EntityFile getParent() {
        return parent;
    }

    public void setParent(EntityFile parent) {
        this.parent = parent;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getSubfolder() {
        return subfolder;
    }

    public void setSubfolder(String subfolder) {
        this.subfolder = subfolder;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
