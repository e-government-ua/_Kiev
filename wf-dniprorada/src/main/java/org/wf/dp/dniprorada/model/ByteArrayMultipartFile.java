package org.wf.dp.dniprorada.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ByteArrayMultipartFile implements MultipartFile {

    private InputStream inputStream;
    private byte[] content;
    private String name;
    private String contentType;
    private String exp;
    private String originalFilename;

    public ByteArrayMultipartFile(InputStream inputStream, String name,
            String originalFilename, String contentType) {
        this.inputStream = inputStream;
        this.name = name;
        this.originalFilename = originalFilename;
        if(contentType == null){
        	throw new IllegalArgumentException(
        			String.format("Content type of file [name:%s|originalName:%s] is null",
        					name, originalFilename));
        }
        String[] contentSplit = contentType.split(";"); //в типе контента содержится расширение файла image/jpeg;jpg
        this.contentType = contentSplit[0];
        this.exp = contentSplit[1];

        List<Byte> contentByteList = new ArrayList<Byte>();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        try {
            int data = bufferedInputStream.read();
            while (data != -1) {
                contentByteList.add((byte) data);
                data = bufferedInputStream.read();
            }
            content = new byte[contentByteList.size()];
            for (int i = 0; i < contentByteList.size(); i++) {
                 content[i] = contentByteList.get(i);
            }
        } catch (IOException ex) {
            content = ex.getMessage().getBytes();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }
    
    public String getExp() {
        return exp;
    }

    @Override
    public boolean isEmpty() {
        return content != null && content.length > 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

   @Override
    public byte[] getBytes() {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(content);
    }
}