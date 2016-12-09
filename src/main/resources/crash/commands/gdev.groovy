package crash.commands

import org.springframework.core.io.Resource

(scan.resources { 'file:../genesys-generator/src/main/resources/metashift/**' } | { Resource r ->

    if(r.file.isDirectory() && !r.file.isHidden()) {
        addCmdMnt r.file.canonicalPath
    }

} )()
