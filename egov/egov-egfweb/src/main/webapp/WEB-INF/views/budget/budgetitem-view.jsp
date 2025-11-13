

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn"%>

    <style>
        /* Stronger selector and !important to override Bootstrap defaults */
        thead.table-header th {
            background-color: #003366 !important;
            color: #fff !important;
            text-align: center;
            vertical-align: middle;
            font-weight: 700;
            font-size:medium;
        }

        .section-header {
            background-color: #d9edf7;
            font-weight: bold;
            text-align: center;
        }

        .sub-header {
            background-color: #fce4d6;
            font-weight: bold;
        }

        .category-header {
            background-color: #f2f2f2;
            font-weight: bold;
        }

        .total-row {
            background-color: #eeeeee;
            font-weight: bold;
        }
    </style>


    <div class="table-responsive">

        hello

        ${budgetGroup}

        <c:forEach var="entry" items="${budgetGroups}">
            <h3>${entry.key}</h3>

            <table border="1">
                <tr>
                    <th>Function Code</th>
                    <th>Budget Group</th>
                    <th>Budget Code</th>
                    <th>Next Estimate</th>
                </tr>

                <c:forEach var="item" items="${entry.value}">
                    <tr>
                        <td>${item.function.code}</td>
                        <td>${item.budgetGroup}</td>
                        <td>${item.budgetCode}</td>
                        <td>${item.nextEstimate}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:forEach>


        <!--<table class="table table-bordered table-striped align-middle">
            <thead class="table-header">
                <tr>
                    <th>Budget Head</th>
                    <th>Budget Code</th>
                    <th>Budget Estimate 2025-26</th>
                    <th>Actuals 2025-26 (9 months)</th>
                    <th>Revised Estimate 2025-26</th>
                    <th>Budget Estimate 2026-27</th>
                </tr>
            </thead>
            <tbody>
                <tr class="section-header">
                    <td colspan="6">3 - Finance, Accounts and Audits</td>
                </tr>
                <tr class="sub-header">
                    <td colspan="6">Part A - REVENUE BUDGET</td>
                </tr>

                <tr>
                    <td colspan="2"><strong>Opening balance as on 01.04.2025</strong></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr class="category-header">
                    <td colspan="6">Revenue Receipts</td>
                </tr>

                <tr>
                    <td colspan="6"><strong>Rental Income from Municipal Properties</strong></td>
                </tr>
                <tr>
                    <td>Rent from vehicles</td>
                    <td>03-RR-040</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Sale of scraps</td>
                    <td>03-RR-041</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr>
                    <td colspan="6"><strong>Fees &amp; User Charges</strong></td>
                </tr>
                <tr>
                    <td>Certificate fees</td>
                    <td>03-RR-023</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr class="category-header">
                    <td colspan="6">Revenue Expenditure</td>
                </tr>

                <tr>
                    <td colspan="6"><strong>Establishment Expenses</strong></td>
                </tr>
                <tr>
                    <td>Salaries &amp; Allowances</td>
                    <td>03-RE-001</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Benefits &amp; Allowances</td>
                    <td>03-RE-002</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Pension</td>
                    <td>03-RE-003</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="total-row">
                    <td>Total</td>
                    <td colspan="5"></td>
                </tr>

                <tr>
                    <td colspan="6"><strong>Termination &amp; Retirement Benefits</strong></td>
                </tr>
                <tr>
                    <td>Leave Encashment</td>
                    <td>03-RE-004</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Gratuity and retirement benefits</td>
                    <td>03-RE-005</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="total-row">
                    <td>Total</td>
                    <td colspan="5"></td>
                </tr>

                <tr>
                    <td colspan="6"><strong>Administrative Expenses</strong></td>
                </tr>
                <tr>
                    <td>Office contingencies</td>
                    <td>03-RE-006</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Printing and stationery</td>
                    <td>03-RE-007</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Professional fees</td>
                    <td>03-RE-008</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Electricity charges</td>
                    <td>03-RE-009</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </tbody>
        </table>-->
    </div>