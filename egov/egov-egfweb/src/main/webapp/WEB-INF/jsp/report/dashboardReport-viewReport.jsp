
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<div>
    <h1>ULB Dashboard </h1>


    <p>Total Contractors: <s:property value="contractors.size()" /></p>

    <p>Total Suppliers: <s:property value="totalSuppliers" /></p>

    <p>Total Expense Bills Created: <s:property value="totalExpenseBills" /></p>

    <p>Total Bills Created: <s:property value="totalBills" /></p>

    <p>Total Bills Amount: <s:property value="totalAmount" /></p>

<!--    <s:iterator value="contractors" var="contractor">-->
<!--        <p>Contractor Name: <s:property value="name" /><
    },
    {/p>-->
<!--    </s:iterator>-->


</div>