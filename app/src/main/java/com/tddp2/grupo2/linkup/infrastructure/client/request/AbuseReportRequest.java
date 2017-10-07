package com.tddp2.grupo2.linkup.infrastructure.client.request;

import com.tddp2.grupo2.linkup.model.AbuseReport;

import java.io.Serializable;

public class AbuseReportRequest implements Serializable {

    private AbuseReport abuseReport;

    public AbuseReportRequest(AbuseReport abuseReport) {
        this.abuseReport = abuseReport;
    }

    public AbuseReport getAbuseReport() {
        return abuseReport;
    }

    public void setAbuseReport(AbuseReport abuseReport) {
        this.abuseReport = abuseReport;
    }
}
