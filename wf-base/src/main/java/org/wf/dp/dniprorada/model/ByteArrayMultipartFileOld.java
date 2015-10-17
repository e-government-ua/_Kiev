package org.wf.dp.dniprorada.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteArrayMultipartFileOld implements MultipartFile {
    private static final Logger LOG = LoggerFactory.getLogger(ByteArrayMultipartFileOld.class);

    private InputStream inputStream;
    private byte[] content;
    private String name;
    private String contentType;
    private String exp;
    private String originalFilename;

    public ByteArrayMultipartFileOld(InputStream inputStream, String name,
            String originalFilename, String contentType) {
        this.inputStream = inputStream;
        this.name = name;
        this.originalFilename = originalFilename;
        if (contentType == null) {
            throw new IllegalArgumentException(
                    String.format("Content type of file [name:%s|originalName:%s] is null",
                            name, originalFilename));
        }
        String[] contentSplit = contentType.split(";"); //в типе контента содержится расширение файла image/jpeg;jpg
        if (contentSplit.length == 2) {
            this.contentType = contentSplit[0];
            this.exp = contentSplit[1];
        } else if (contentType.startsWith("image")) {
            this.contentType = contentType;
            this.exp = contentType.split("/")[1];
        } else {
            this.contentType = contentType;
            this.exp = null;
        }

        List<Byte> contentByteList = new ArrayList<Byte>();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        try {
            int data = bufferedInputStream.read();
            while (data != -1) {
                contentByteList.add((byte) data);
                data = bufferedInputStream.read();
            }
            LOG.debug(" ||||| " + contentByteList.size() + " ||||| " + contentByteList);
            content = new byte[contentByteList.size()];
            for (int i = 0; i < contentByteList.size(); i++) {
                content[i] = contentByteList.get(i);
            }
            LOG.debug(Arrays.toString(content));
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