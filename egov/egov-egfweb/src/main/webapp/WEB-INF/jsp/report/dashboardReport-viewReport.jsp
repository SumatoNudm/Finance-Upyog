<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<div>
    <h3>Dashboard</h3>

    <div class="row">
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Expense Bills</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalExpenseBills" /></h5>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Contractor Bills</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalContractorBills" /></h5>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Supplier Bill</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalSupplierBills" /></h5>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Work Orders</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalWorkOrders" /></h5>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Purchase Orders</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalPurchaseOrders" /></h5>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Journal Vouchers</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalJournalVouchers" /></h5>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Funds</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalFunds" /></h5>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Bank Accounts</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalBankAccounts" /></h5>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Contractors</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalContractors" /></h5>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading text-center">Total Suppliers</div>
                <div class="panel-body text-center">
                    <h5><s:property value="totalSuppliers" /></h5>
                </div>
            </div>
        </div>


    </div>

    <!--    <s:iterator value="contractors" var="contractor">-->
    <!--        <p>Contractor Name: <s:property value="name" /><
        },
        {/p>-->
    <!--    </s:iterator>-->


</div>