<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PLAB Java SDK Sample</title>

<script type="text/javascript" src="/js/experimentMetaInfo.js"></script>
<script type="text/javascript" src="/js/plab-web-sdk-0.7.0.min.js?201803270900"></script>
<script>
    var plabPageKey = '/cellphone_shop/main_test';
    try{
        var params = {
            projectKey : "11st_mweb",
            domain : "11st.co.kr",
            onConsole : false,
            onApiSync : false,
            onTrackSync : true,
            onManualVisit : true
        };
        plab.initBy(params, experimentMetaInfo);
        plab.redirect(plabPageKey);

    }catch (e) { console.log(e) };
</script>

</head>
<body>
<h2>PLAB Java SDK Sample</h2>
<script> document.write("<h3>Experiment Key: " + plabPageKey + "</h3>")</script>
<script> document.write("<h3>Variation: " + plab.getVariation(plabPageKey) + "</h3>")</script>
</body>
</html>