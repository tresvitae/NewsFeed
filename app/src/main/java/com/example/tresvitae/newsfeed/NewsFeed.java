package com.example.tresvitae.newsfeed;

public class NewsFeed {

    /** Declare variables for set a data. */
    private String fieldSectionName;
    private String fieldWebTitle;
    private String fieldWebPublicationDate;
    private String fieldWebUri;
    private String fieldAuthorName;

    /** Set construct a new {@link NewsFeed} object.
     * @param sectionName is the title of the article
     * @param webTitle is the author of the article
     * @param webPublicationDate is the publication date of the article
     * @param webUri is the url to the article
     * @param authorName is the name of the article's author
     */
    public NewsFeed (String sectionName, String webTitle, String webPublicationDate, String webUri, String authorName){
        fieldSectionName = sectionName;
        fieldWebTitle = webTitle;
        fieldWebPublicationDate = webPublicationDate;
        fieldWebUri = webUri;
        fieldAuthorName = authorName;
    }
    /** Return the information */
    public String getSectionName() {
        return fieldSectionName;
    }

    public String getWebTitle() {
        return fieldWebTitle;
    }

    public String getWebPublicationDate() {
        return fieldWebPublicationDate;
    }

    public String getWebUri() {
        return fieldWebUri;
    }

    public String getAuthorName() {
        return fieldAuthorName;
    }
}