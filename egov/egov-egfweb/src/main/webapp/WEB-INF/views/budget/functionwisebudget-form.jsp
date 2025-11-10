<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<form action="${pageContext.request.contextPath}/budget/save" method="post" id="budgetForm">
    <!-- Top header row -->
    <table>
        <thead>
        <tr>
            <th style="width:35%;">Budget Head</th>
            <th style="width:12%;">Code</th>
            <th style="width:12%;">Major Code</th>
            <th style="width:13%;">Budget Estimate 2024-25</th>
            <th style="width:13%;">Actuals 2024-25 (9 months)</th>
            <th style="width:13%;">Revised Estimate 2024-25</th>
            <th style="width:12%;">Budget Estimate 2025-26</th>
        </tr>
        </thead>
    </table>

    <!-- Iterate categories (sections) -->
    <c:forEach var="entry" items="${groupedHeads}">
        <c:set var="category" value="${entry.key}" />
        <div class="section">${category}</div>

        <table data-category="${category}">
            <tbody>
            <c:forEach var="bh" items="${entry.value}">
                <tr>
                    <td style="width:35%;">
                        <input type="hidden" name="bhId" value="${bh.id}" />
                        <div><strong>${bh.name}</strong></div>
                        <div class="small">${bh.accountType} - ${bh.program}</div>
                    </td>

                    <td style="width:12%">${bh.code}</td>
                    <td style="width:12%">${bh.accountTypeCode}</td>

                    <!-- Numeric inputs: name pattern value_{id}_colX -->
                    <td style="width:13%;">
                        <input type="number" step="0.01"
                               name="value_${bh.id}_col1"
                               data-bh="${bh.id}"
                               data-cat="${fn:replace(category,' ','_')}"
                               data-col="1"
                               class="number col1_${fn:replace(category,' ','_')}" />
                    </td>

                    <td style="width:13%;">
                        <input type="number" step="0.01"
                               name="value_${bh.id}_col2"
                               data-bh="${bh.id}"
                               data-cat="${fn:replace(category,' ','_')}"
                               data-col="2"
                               class="number col2_${fn:replace(category,' ','_')}" />
                    </td>

                    <td style="width:13%;">
                        <input type="number" step="0.01"
                               name="value_${bh.id}_col3"
                               data-bh="${bh.id}"
                               data-cat="${fn:replace(category,' ','_')}"
                               data-col="3"
                               class="number col3_${fn:replace(category,' ','_')}" />
                    </td>

                    <td style="width:12%;">
                        <input type="number" step="0.01"
                               name="value_${bh.id}_col4"
                               data-bh="${bh.id}"
                               data-cat="${fn:replace(category,' ','_')}"
                               data-col="4"
                               class="number col4_${fn:replace(category,' ','_')}" />
                    </td>
                </tr>
            </c:forEach>

            <!-- section subtotal row -->
            <tr class="subtotal">
                <td colspan="3">Total ${category}</td>
                <td class="right"><input type="text" readonly id="subtotal_${fn:replace(category,' ','_')}_1" /></td>
                <td class="right"><input type="text" readonly id="subtotal_${fn:replace(category,' ','_')}_2" /></td>
                <td class="right"><input type="text" readonly id="subtotal_${fn:replace(category,' ','_')}_3" /></td>
                <td class="right"><input type="text" readonly id="subtotal_${fn:replace(category,' ','_')}_4" /></td>
            </tr>
            </tbody>
        </table>
    </c:forEach>

    <!-- Grand totals -->
    <table>
        <tr class="grand">
            <td colspan="3">Total Revenue Expenditure</td>
            <td class="right"><input type="text" readonly id="grand_1"/></td>
            <td class="right"><input type="text" readonly id="grand_2"/></td>
            <td class="right"><input type="text" readonly id="grand_3"/></td>
            <td class="right"><input type="text" readonly id="grand_4"/></td>
        </tr>
    </table>

    <button type="submit">Save</button>
    <button type="reset" id="resetBtn">Reset</button>
</form>

<script>
    // helper: format to 2 decimals (empty string if 0)
    function fmt(x){ return isNaN(x) ? '' : parseFloat(x).toFixed(2); }

    // compute subtotal for a category and column
    function subtotalFor(catSafe, col){
      const inputs = document.querySelectorAll('input[data-cat="'+catSafe+'"][data-col="'+col+'"]');
      let s = 0;
      inputs.forEach(i => {
        const v = parseFloat(i.value);
        if (!isNaN(v)) s += v;
      });
      return s;
    }

    // update all subtotals and grand totals
    function updateAllTotals(){
      // find all distinct categories from inputs
      const inputs = document.querySelectorAll('input[data-cat]');
      const cats = new Set();
      inputs.forEach(i => cats.add(i.getAttribute('data-cat')));

      let grand = [0,0,0,0];
      cats.forEach(cat => {
        for (let c=1;c<=4;c++){
          const s = subtotalFor(cat, c);
          document.getElementById('subtotal_'+cat+'_'+c).value = fmt(s);
          grand[c-1] += s;
        }
      });

      document.getElementById('grand_1').value = fmt(grand[0]);
      document.getElementById('grand_2').value = fmt(grand[1]);
      document.getElementById('grand_3').value = fmt(grand[2]);
      document.getElementById('grand_4').value = fmt(grand[3]);
    }

    // listen to number inputs
    document.addEventListener('input', function(e){
      if (e.target && e.target.matches('input.number')) {
        updateAllTotals();
      }
    });

    // recalc after reset (delay to allow reset to complete)
    document.getElementById('resetBtn').addEventListener('click', function(){
      setTimeout(updateAllTotals, 10);
    });

    // initialize totals on load
    window.addEventListener('load', function(){ updateAllTotals(); });
</script>
