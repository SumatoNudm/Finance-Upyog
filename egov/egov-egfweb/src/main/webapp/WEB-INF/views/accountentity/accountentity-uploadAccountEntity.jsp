<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>

<c:if test="${not empty message}">
    <div class="alert alert-success"><c:out value="${message}" /></div>
</c:if>
<c:if test="${not empty skippedMessages}">
    <div class="alert alert-warning">
        <c:forEach items="${skippedMessages}" var="s">
            <div><c:out value="${s}" /></div>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">
        <c:forEach items="${fn:split(errorMessage, ';')}" var="err">
            <div><c:out value="${err}" /></div>
        </c:forEach>
    </div>
</c:if>

<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/exceljs/4.4.0/exceljs.min.js"></script>
<c:url value="/accountentity/createFromUpload" var="createFromUploadUrl"/>
<form:form role="form" modelAttribute="accountEntity" action="/services/EGF/accountentity/createFromUpload" id="accountEntityForm"
           class="form-horizontal form-groups-bordered"  method="post">

<input type="hidden"
                      name="${_csrf.parameterName}"
                      value="${_csrf.token}" />

    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title"><spring:message code="lbl.user.defined.code" text=" User Defined Code"/></div>
                </div>
                <div class="panel-body">

                    <%-- Row 1: Account detail type --%>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right">
                            <spring:message code="lbl.accountdetailtype" text="Account detail type"/>
                            <span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select path="accountdetailtype" id="accountdetailtype"
                                         cssClass="form-control" cssErrorClass="form-control error"
                                         required="required">
                                <form:option value="">
                                    <spring:message code="lbl.select" text="Select"/>
                                </form:option>
                                <form:options items="${accountdetailtypes}" itemValue="id" itemLabel="name" />
                            </form:select>
                            <form:errors path="accountdetailtype" cssClass="error-msg" />
                        </div>

                        <%-- Hidden field carries the parsed Excel data as JSON --%>
                        <input type="hidden" name="accountEntities" id="accountEntities" />
                    </div>

                    <%-- Row 2: Upload Sheet label, file chooser under the dropdown, Download Template on the right --%>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right">
                            Upload Sheet <span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <s:file name="accountEntityFile" id="accountEntityFile" />
                        </div>
                        <div class="col-sm-3 add-margin">
                            <a href="javascript:void(0);" onclick="downloadTemplate();">
                                <i class="fa fa-download"></i> Download Template
                            </a>
                        </div>
                    </div>

                    <%-- Row 3: message area (red errors show here) --%>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <span id="fileError"></span>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form:form>

<div class="text-center">
    <%-- Button is OUTSIDE the form; we trigger submission manually after FileReader finishes --%>
    <button type='button' class='btn btn-primary' id="buttonSubmit" onclick="handleUpload();">
        <spring:message code='lbl.create' text="Create"/>
    </button>
    <a href='javascript:void(0)' class='btn btn-default' onclick="javascript:window.parent.postMessage('close','*');"><spring:message code='lbl.close' text="Close"/></a>



</div>

<script>


function downloadTemplate() {


    var wb = new ExcelJS.Workbook();
    var ws = wb.addWorksheet("AccountEntities");

    ws.columns = [
        { header: "Sr No",    key: "srNo",      width: 8 },
        { header: "Name*",     key: "name",      width: 30 },
        { header: "Code*",     key: "code",      width: 15 },
        { header: "Narration", key: "narration", width: 40 },
        { header: "Active",    key: "active",    width: 10 }
    ];

    ws.addRows([
        [1, "Sample Entity 1", "12345", "Sample narration", "Yes"]
    ]);

    ws.getRow(1).font = { bold: true };

    // Yes/No dropdown on the Active column (E), rows 2 to 1000
    for (var r = 2; r <= 1000; r++) {
        ws.getCell("E" + r).dataValidation = {
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
        a.download = "subledger_master_bulk_upload.xlsx";
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(a.href);
    }).catch(function (e) {
        console.error("Template download failed", e);
    });
}


     function showFileMessage(text, isError) {
              var el = document.getElementById("fileError");
              el.style.color = isError ? "#d9534f" : "#555";
              el.style.fontWeight = isError ? "bold" : "normal";
              el.innerHTML = text; }



    function handleUpload() {
        var fileError = document.getElementById("fileError");
        showFileMessage("", false);

        // 1. Validate account detail type selection
        if (!document.getElementById("accountdetailtype").value) {
            showFileMessage("Please select an Account detail type.", true);
            return;
        }

        // 2. Validate file selection
        var fileInput = document.getElementById("accountEntityFile");
        var file = fileInput.files[0];

        if (!file) {
            showFileMessage("Please upload a valid Excel file (.xlsx or .xls).", true);
            return;
        }

        // 3. Validate file extension
        var fileName = file.name;
        var validExtensions = [".xlsx", ".xls"];
        var isValid = validExtensions.some(function(ext) { return fileName.endsWith(ext); });

        if (!isValid) {
            showFileMessage("Invalid file format. Please upload an Excel file (.xlsx or .xls).", true);
            return;
        }

        // 4. Disable button to prevent double-submit
        document.getElementById("buttonSubmit").disabled = true;
        showFileMessage("Processing file, please wait...", false);

        var reader = new FileReader();

        reader.onload = function (e) {
            try {
                var data = new Uint8Array(e.target.result);
                var workbook = XLSX.read(data, { type: "array" });

                var sheetName = workbook.SheetNames[0];
                var sheet = workbook.Sheets[sheetName];


               // Validate header row before parsing
               var expectedHeaders = ["Sr No", "Name*", "Code*", "Narration", "Active"];
               var headerRows = XLSX.utils.sheet_to_json(sheet, { header: 1, raw: false, defval: "" });
               var actualHeaders = (headerRows[0] || []).map(function (h) { return String(h).trim(); });



               while (actualHeaders.length && actualHeaders[actualHeaders.length - 1] === "") {
                  actualHeaders.pop();
                }


                var headersOk = actualHeaders.length === expectedHeaders.length &&
                    expectedHeaders.every(function (h, i) { return h === actualHeaders[i]; });

                if (!headersOk) {
                    showFileMessage("Invalid file headers. Expected: " + expectedHeaders.join(", ") +
                        ". Found: " + (actualHeaders.join(", ") || "(empty)") +
                        ". Please use the provided template.", true);
                    document.getElementById("buttonSubmit").disabled = false;
                    return;
                }

                // Parse with raw:false so all values become strings; skip the header row (offset:1)
                // Use header:1 to get raw arrays, then map manually so field names match
                // the @SerializedName keys in AccountEntityUploadRow
                var rawRows = XLSX.utils.sheet_to_json(sheet, {
                    raw: false,
                    defval: "",
                    header: ["Sr No", "Name*", "Code*", "Narration", "Active"]
                });

                // rawRows[0] is the header row itself — remove it
                if (rawRows.length > 0) {
                    rawRows.shift();
                }

                if (rawRows.length === 0) {
                    showFileMessage("The uploaded file contains no data rows.", true);
                    document.getElementById("buttonSubmit").disabled = false;
                    return;
                }

                var jsonPayload = JSON.stringify(rawRows);
                console.log("Parsed Excel JSON:", jsonPayload);

                document.getElementById("accountEntities").value = jsonPayload;

                // Now submit the form
                document.getElementById("accountEntityForm").submit();

            } catch (err) {
                showFileMessage("Error reading file: " + err.message, true);
                document.getElementById("buttonSubmit").disabled = false;
            }
        };

        reader.onerror = function () {
            showFileMessage("Failed to read the file. Please try again.", true);
            document.getElementById("buttonSubmit").disabled = false;
        };

        reader.readAsArrayBuffer(file);
    }

</script>