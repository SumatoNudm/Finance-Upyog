$(document).ready(function () {
    var functionName = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/services/EGF/common/ajaxfunctionnames?name=%QUERY',
            filter: function (data) {
                return $.map(data, function (ct) {
                    return {
                        code: ct.split("~")[0].split("-")[0],
                        name: ct.split("~")[0].split("-")[1],
                        id: ct.split("~")[1],
                        codeName: ct
                    };
                });
            }
        }
    });

    functionName.initialize();
    $('#function').typeahead({
        hint: true,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'codeName',
        source: functionName.ttAdapter()
    }).on('typeahead:selected', function (event, data) {
        $(".cfunction").val(data.id);
    });

});
$('#function').blur(function () {
    if ($('.cfunction').val() == "") {
        bootbox.alert("Please select function from dropdown values", function () {
            $('#function').val("");
        });
    }
});