$(document).on('click', '.accordion-toggle', function(event) {
        event.stopPropagation();
        var $this = $(this);
        var parent = $this.data('parent');
        var actives = parent && $(parent).find('.collapse.in');
        if (actives && actives.length) {
            hasData = actives.data('collapse');
            actives.collapse('hide');
        }
        var target = $this.attr('data-target') || (href = $this.attr('href')) && href.replace(/.*(?=#[^\s]+$)/, ''); 
        $(target).collapse('toggle');
})