package com.tddp2.grupo2.linkup.task;

import com.tddp2.grupo2.linkup.model.Links;

public class AcceptLinkTaskResponse extends TaskResponse {
    private boolean isAMatch;
    private String matchName;
    private Links links;

    public AcceptLinkTaskResponse() {}

    public AcceptLinkTaskResponse(String error) {
        super(error);
    }

    public void setIsAMatch(boolean isAMatch) {
        this.isAMatch = isAMatch;
    }

    public boolean isAMatch() {
        return this.isAMatch;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getMatchName() {
        return this.matchName;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Links getLinks() {
        return  this.links;
    }
}
