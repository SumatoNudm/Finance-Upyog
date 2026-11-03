package org.egov.commons.dao;

import java.util.ArrayList;
import java.util.List;

public class AccountdetailtypeUploadResult {
    private int saved;
    private List<String> skipped = new ArrayList<>();

    public int getSaved() { return saved; }
    public void setSaved(int saved) { this.saved = saved; }
    public List<String> getSkipped() { return skipped; }
}