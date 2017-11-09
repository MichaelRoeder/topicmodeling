package org.dice_research.topicmodeling.wikipedia.sweble;

@Deprecated
public class SwebleWikitextAdapter {

    public String getCleanText(String text) throws Exception {
        // WikiConfig config = DefaultConfigEn.generate();
        // WtEngine engine = new WtEngine(config);
        //
        // // Retrieve a page
        // PageTitle pageTitle = PageTitle.make(config, "title");
        //
        // PageId pageId = new PageId(pageTitle, -1);
        //
        // // Compile the retrieved page
        // EngCompiledPage cp = engine.postprocess(pageId, text, null);
        //
        // TextConverter p = new TextConverter(config, Integer.MAX_VALUE);
        // p.setCsvOrReadable(false);
        //
        // return (String) p.go(cp.getPage());
        return text;
    }
}
