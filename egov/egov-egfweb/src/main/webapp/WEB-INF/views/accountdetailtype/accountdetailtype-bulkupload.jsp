<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>

<c:if test="${not empty message}">
    <div class="alert alert-success"><c:out value="${message}" /></div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">
        <c:forEach items="${fn:split(errorMessage, ';')}" var="err">
            <div><c:out value="${err}" /></div>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty skippedMessages}">
    <div class="alert alert-warning">
        <c:forEach items="${skippedMessages}" var="s">
            <div><c:out value="${s}" /></div>
        </c:forEach>
    </div>
</c:if>

<form:form role="form" method="post"
           action="/services/EGF/accountdetailtype/bulkupload"
           id="bulkUploadForm" class="form-horizontal form-groups-bordered">

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="hidden" id="accountdetailtypes" name="accountdetailtypes" />

    <div class="form-group">
        <label class="col-sm-3 control-label text-right">
            Upload Excel (Name*, Description*, Active) <span class="mandatory"></span>
        </label>
        <div class="col-sm-4 add-margin">
            <input type="file" id="accountdetailtypeFile" accept=".xlsx,.xls" style="display:none"
                   onchange="document.getElementById('fileName').innerText = this.files[0] ? this.files[0].name : '';" />
            <button type="button" class="btn btn-default"
                    onclick="document.getElementById('accountdetailtypeFile').click();">Choose file</button>
            <span id="fileName"></span>
            <span id="fileError" class="error-msg"></span>
        </div>
    </div>

    <div class="text-center">
        <button type="button" class="btn btn-primary" onclick="return validateForm();">Upload</button>
        <a href="javascript:void(0)" class="btn btn-default"
           onclick="javascript:window.parent.postMessage('close','*');">Close</a>
    </div>

    <button type="button" class="btn btn-default" onclick="downloadTemplate();">
        <i class="fa fa-download"></i> Download Template
    </button>




</form:form>

<script src="https://cdn.sheetjs.com/xlsx-0.20.3/package/dist/xlsx.full.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/exceljs/4.4.0/exceljs.min.js"></script>

<script>

function downloadTemplate() {
    var wb = new ExcelJS.Workbook();
    var ws = wb.addWorksheet("AccountDetailTypes");

    ws.columns = [
        { header: "Name*",        key: "name",        width: 30 },
        { header: "Description*", key: "description", width: 50 },
        { header: "Active",       key: "active",      width: 10 }
    ];

    ws.addRows([
        ["Project",  "Project related account details",      "Yes"]
    ]);

    ws.getRow(1).font = { bold: true };

    // Yes/No dropdown on the Active column (rows 2 to 1000)
    for (var r = 2; r <= 1000; r++) {
        ws.getCell("C" + r).dataValidation = {
            type: "list",
            allowBlank: true,
            formulae: ['"Yes,No"'],
            showErrorMessage: true,
            errorStyle: "stop",
            errorTitle: "Invalid value",
            error: "Please select Yes or No"
        };
    }

    wb.xlsx.writeBuffer().then(function (buffer) {
        var blob = new Blob([buffer], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        });
        var a = document.createElement("a");
        a.href = URL.createObjectURL(blob);
        a.download = "accountdetailtype_template.xlsx";
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(a.href);
    }).catch(function (e) {
        console.error("Template download failed", e);
    });
}

// Yes/No (or true/false, 1/0) -> real boolean; blank = active
function toBool(v) {
    var s = String(v == null ? "" : v).trim().toLowerCase();
    if (s === "") return true;
    return s === "yes" || s === "y" || s === "true" || s === "1" || s === "active";
}

async function validateForm() {
    var fileError = document.getElementById("fileError");
    fileError.innerHTML = "";

    var file = document.getElementById("accountdetailtypeFile").files[0];
    if (!file) {
        fileError.innerHTML = "Please select an Excel file.";
        return false;
    }

    var lower = file.name.toLowerCase();
    if (!lower.endsWith(".xlsx") && !lower.endsWith(".xls")) {
        fileError.innerHTML = "Invalid file format. Please upload .xlsx or .xls file";
        return false;
    }

    try {
        var arrayBuffer = await file.arrayBuffer();
        var workbook = XLSX.read(arrayBuffer);
        var worksheet = workbook.Sheets[workbook.SheetNames[0]];

        var records = XLSX.utils.sheet_to_json(worksheet, {
            header: 1,
            defval: "",
            blankrows: true
        });

        // drop trailing blank rows, keep the rest in order so "Row N" matches Excel
        function isBlank(r) {
            return !String(r[0] || "").trim() && !String(r[1] || "").trim() && !String(r[2] || "").trim();
        }
        while (records.length > 1 && isBlank(records[records.length - 1])) {
            records.pop();
        }

        var data = [];
        for (var i = 1; i < records.length; i++) {          // row 0 = header
            data.push({
                name: String(records[i][0] || "").trim(),
                description: String(records[i][1] || "").trim(),
                active: toBool(records[i][2])
            });
        }

        if (data.length === 0) {
            fileError.innerHTML = "File has no data rows.";
            return false;
        }

        document.getElementById("accountdetailtypes").value = JSON.stringify(data);
        document.getElementById("bulkUploadForm").submit();

    } catch (error) {
        console.error("Error reading Excel file:", error);
        fileError.innerHTML = "Unable to read Excel file.";
    }
    return false;
}
</script>