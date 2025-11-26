<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="container">
    <div class="row">
        <div class="col-md-12">

            <div class="panel panel-default">
                <div class="panel-heading clearfix">
                    <h4 class="panel-title pull-left" style="padding-top:6px;">Budget Registers</h4>
                    <div class="pull-right">
                        <a href="${pageContext.request.contextPath}/budget/register/new" class="btn btn-primary btn-sm">
                            Create New
                        </a>
                    </div>
                </div>

                <div class="panel-body">


                    <hr/>

                    <div class="table-responsive">
                        <table class="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th>Register No.</th>
                                <th>Name</th>
                                <th>Financial Year</th>
                                <th>Status</th>
                                <th>Created Date</th>
                                <!--<th class="text-center">Actions</th>-->
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="budgetRegister" items="${budgetRegisters}">
                                <tr>
                                    <td><c:out value="${budgetRegister.budgetRegisterNumber}" /></td>
                                    <td><c:out value="${budgetRegister.budgetRegisterName}" /></td>
                                    <td><c:out value="${budgetRegister.financialYear.finYearRange}" /></td>
                                    <td>
                                        <c:out value="${budgetRegister.status.code}" />
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${budgetRegister.createdDate}" pattern="dd-MMM-yyyy HH:mm" />
                                    </td>
                                </tr>
                            </c:forEach>


                            </tbody>
                        </table>
                    </div>


                </div> <!-- panel-body -->
            </div> <!-- panel -->

        </div>
    </div>
</div>
