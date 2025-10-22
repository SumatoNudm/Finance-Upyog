<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Budget Heads</title>
    <style>
        body { font-family: Arial, sans-serif; }
        h2 { color: #2c3e50; margin-top: 30px; }
        table { border-collapse: collapse; width: 80%; margin-bottom: 20px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<h1>Budget Heads Overview</h1>

<h2>Revenue Receipts (RR)</h2>
<table>
    <tr>
        <th>Name</th>
        <th>Code</th>
        <th>Program / Scheme applicable</th>
    </tr>
    <c:forEach var="head" items="${rr}">
        <tr>
            <td>${head.name}</td>
            <td>${head.code}</td>
            <td>${head.program}</td>
        </tr>
    </c:forEach>
</table>

<h2>Revenue Expenditure (RE)</h2>
<table>
    <tr>
        <th>Name</th>
        <th>Code</th>
        <th>Program / Scheme applicable</th>
    </tr>
    <c:forEach var="head" items="${re}">
        <tr>
            <td>${head.name}</td>
            <td>${head.code}</td>
            <td>${head.program}</td>
        </tr>
    </c:forEach>
</table>

<h2>Capital Receipts (CR)</h2>
<table>
    <tr>
        <th>Name</th>
        <th>Code</th>
        <th>Program / Scheme applicable</th>
    </tr>
    <c:forEach var="head" items="${cr}">
        <tr>
            <td>${head.name}</td>
            <td>${head.code}</td>
            <td>${head.program}</td>
        </tr>
    </c:forEach>
</table>

<h2>Capital Expenditure (CE)</h2>
<table>
    <tr>
        <th>Name</th>
        <th>Code</th>
        <th>Program / Scheme applicable</th>
    </tr>
    <c:forEach var="head" items="${ce}">
        <tr>
            <td>${head.name}</td>
            <td>${head.code}</td>
            <td>${head.program}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
