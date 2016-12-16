package crash.commands

import org.springframework.core.io.Resource

class NotHiddenFileFilter implements FileFilter{

    boolean accept(File pathname) {
        return !pathname.isDirectory() && !pathname.isHidden()
    }
}

(scan.resources { "file:${System.getProperty("user.home")}/metashift/**-metamods/resources/**" } | { Resource r ->

    if(r.file.isDirectory()
        && !r.file.isHidden()
        && r.file.listFiles(new NotHiddenFileFilter()).length > 0) {

        addCmdMnt r.file.canonicalPath
    }

} )()
