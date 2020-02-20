$(function(){
    var $container = $('#webhook-url-group');
    var addEntry = function() {
        var tpl = $('#webhook-url-entry');
        var total_entry = $(".entry").length;
        var max = 3;
        var index = total_entry;
        // last entry index + 1
//        var index = 0;
//        if (total_entry > 0)
//            index = Number($(".entry:last").attr("value")) + 1;

        // Append element
        var data = {
            "webhook-url-name": "webhookUrls[" + index + "].name",
            "webhook-url-method": "webhookUrls[" + index + "].method",
            "webhook-url-url": "webhookUrls[" + index + "].url",
            "webhook-url-body": "webhookUrls[" + index + "].body"
        }
        if (total_entry < max )
            $container.append(Mustache.render(tpl.html(), data))
    }
    //console.log("container=" + $container.html());

    // Handle webhook-url-name click
    $container.on('click', '.dropdown-menu a', function(e) {
        var value = $(this).attr('value');
        var tpl = $('#body-' + value);
        $(this).parents(".entry").find(".webhook-url-name").val($(this).text());
        $(this).parents(".entry").find(".webhook-url-body").val(tpl.html());
    });

    // Handle add-entry click
    $container.on('click','.add-entry',function(e) {
        addEntry();
    });

    // Handle remove-entry click
    $container.on('click','.remove-entry',function(e) {
        var total_entry = $(".entry").length;
        if (total_entry > 1)
            $(e.target).parents('fieldset').remove();

        // Reset all inputs' name
        $(".webhook-url-name").each(function(index) {
            $(this).attr("name", "webhookUrls[" + index + "].name")
        })
        $(".webhook-url-method").each(function(index) {
            $(this).attr("name", "webhookUrls[" + index + "].method")
        })
        $(".webhook-url-url").each(function(index) {
            $(this).attr("name", "webhookUrls[" + index + "].url")
        })
        $(".webhook-url-body").each(function(index) {
            $(this).attr("name", "webhookUrls[" + index + "].body")
        })
    });
});
