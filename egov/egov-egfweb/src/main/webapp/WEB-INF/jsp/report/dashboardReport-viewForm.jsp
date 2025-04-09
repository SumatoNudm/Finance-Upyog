<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<div xmlns:s="http://www.w3.org/1999/XSL/Transform">
    <h3>Dashboard</h3>


    <form id="dashboardReportForm"  action="/services/EGF/report/dashboardReport-viewReport.action" method="get" >
        <table align="center" width="100%" cellpadding="0" cellspacing="0">

            <tr>
                <td style="width: 5%"></td>
                <td class="greybox"><s:text name="Start Date" /><span
                        class="mandatory1" id="disableFromDateCheck">*</span></td>
                <s:date name="startDate" format="dd/MM/yyyy" var="tempStartDate" />
                <td class="greybox">
                    <s:textfield id="startDate" name="startDate"
                                 value="%{tempStartDate}"  data-date-end-date="0d"
                                 onkeyup="DateFormat(this,this.value,event,false,'3')"
                                 placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
                                 data-inputmask="'mask': 'd/m/y'" autocomplete="off"/>
                </td>

                <s:date name="endDate" format="dd/MM/yyyy" var="tempEndDate" />
                <td class="greybox"><s:text name="End Date" /><span
                        class="mandatory1" id="disableToDateCheck">*</span></td>
                <td class="greybox">
                    <s:textfield id="endDate" name="endDate"
                                 value="%{tempEndDate}"  data-date-end-date="0d"
                                 onkeyup="DateFormat(this,this.value,event,false,'3')"
                                 placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
                                 data-inputmask="'mask': 'd/m/y'" autocomplete="off"/>

                </td>
            </tr>

        </table>

        <div align="center" class="buttonbottom">
            <s:submit key="lbl.search" onclick="return validateAndSubmit()"
                      cssClass="buttonsubmit" />
            <input type="button" value='<s:text name="lbl.close"/>'
                   onclick="javascript:window.parent.postMessage('close','*');" class="button" />
        </div>

    </form>


    <div id="report" >

    </div>

    <!--    <s:iterator value="contractors" var="contractor">-->
    <!--        <p>Contractor Name: <s:property value="name" /><
        },
        {/p>-->
    <!--    </s:iterator>-->


</div>
