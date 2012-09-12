var results = jQuery("#results").html("<li>Loading...</li>"); 
 
jQuery.get("test.html", function(html){ 
  results.html( html ); 
  assert( results ); 
});
