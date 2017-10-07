package com.tddp2.grupo2.linkup.infrastructure.client.response;

import com.tddp2.grupo2.linkup.model.AbuseReport;

public class AbuseReportResponse {

    private AbuseReport abuseReport;

    public AbuseReportResponse(AbuseReport abuseReport) {
        this.abuseReport = abuseReport;
    }

    public AbuseReport getAbuseReport() {
        return abuseReport;
    }

    public void setAbuseReport(AbuseReport rejection) {
        this.abuseReport = abuseReport;
    }

}
