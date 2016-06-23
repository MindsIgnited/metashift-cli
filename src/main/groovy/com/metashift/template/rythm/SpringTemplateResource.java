package com.metashift.template.rythm;

import org.rythmengine.extension.ITemplateResourceLoader;
import org.rythmengine.resource.ITemplateResource;
import org.rythmengine.resource.TemplateResourceBase;
import org.rythmengine.utils.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.*;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;
/**
 * Created with IntelliJ IDEA.
 * User: luog
 * Date: 25/11/13
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
class SpringTemplateResource extends TemplateResourceBase implements ITemplateResource {

    Logger logger = LoggerFactory.getLogger(ITemplateResource.class);

    private Resource springResource = null;
    private String key;
    private boolean isFile;
    private boolean isValid = true;

    SpringTemplateResource(Resource rsrc, ITemplateResourceLoader loader) {
        super(loader);
        if (null == rsrc) {
            throw new NullPointerException();
        }
        springResource = rsrc;
        isFile = (rsrc instanceof AbstractFileResolvingResource) ||
            (rsrc instanceof FileSystemResource) || (rsrc instanceof VfsResource);

        if (!isFile) {
            key = reload();
        } else {
            try {
                key = getPath(rsrc);
                if (!key.startsWith("/")) {
                    key = ("/" + key).replace(loader.getResourceLoaderRoot(), "");
                } else {
                    key = key.replace(loader.getResourceLoaderRoot(), "");
                }
                isValid = rsrc.isReadable();
            } catch (IOException e) {
                logger.warn("error loading spring resources:"+rsrc.getFilename(),e);
                try {
                    key = reload();
                } catch (RuntimeException e1) {
                    isValid = false;
                }
                isFile = false;
            }
        }
    }

    private String getPath(Resource rsrc) throws IOException {
        if (rsrc instanceof ContextResource) {
            return ((ContextResource)rsrc).getPathWithinContext();
        }
        if (rsrc instanceof ClassPathResource) {
            return ((ClassPathResource)rsrc).getPath();
        }
        if (rsrc instanceof AbstractFileResolvingResource) {
            return rsrc.getFile().getPath();
        }
        if (rsrc instanceof FileSystemResource) {
            return ((FileSystemResource)rsrc).getPath();
        }
        throw new IOException("Cannot find resources path");
    }

    @Override
    public Object getKey() {
        return key;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    protected long defCheckInterval() {
        return isFile ? 1000 * 5 : -1;
    }

    @Override
    protected long lastModified() {
        if (!isFile) return 0;
        try {
            return springResource.lastModified();
        } catch (IOException e) {
            logger.warn("error refreshing spring resources: "+springResource.getFilename(),e);
            return -1;
        }
    }

    @Override
    protected String reload() {
        if (!isFile) {
            return key;
        }
        try {
            if (getEngine().isDevMode()) {
                URL url = springResource.getURL();
                if (ResourceUtils.URL_PROTOCOL_FILE.equals(url.getProtocol())) {
                    // this ensures it always load the file changes
                    // instead of cached content instream the inputstream
                    return IO.readContentAsString(springResource.getFile());
                }
            }
            return IO.readContentAsString(springResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSuggestedClassName() {
        return path2CN(key);
    }
}
