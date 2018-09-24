package org.gracilianomp.treetemplate;

public interface TreeEntryFilter {

    TreeEntryFilter NO_FILTER = new NoFilter() ;

    class NoFilter implements TreeEntryFilter {
        @Override
        public boolean accept(String path) {
            return true;
        }
    }

    TreeEntryFilter GITIGNORE_FILTER = new GitignoreFilter() ;

    class GitignoreFilter implements TreeEntryFilter {
        @Override
        public boolean accept(String path) {
            if (path.matches("(?:^\\.git/.*|^/?\\.git/?$|/^.*?/.git/.*)")) return false ;
            if (path.matches("(?:^__MACOSX/.*|^/?__MACOSX/?$|/^.*?/__MACOSX/.*)")) return false ;
            if (path.matches("(?:^\\.DS_Store/.*|^/?\\.DS_Store/?$|/^.*?/\\.DS_Store/.*)")) return false ;
            return true ;
        }
    }

    boolean accept(String path) ;

}
