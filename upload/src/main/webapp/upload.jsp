<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>File Upload Sample</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">
    <p>
        <label>Select a file: </label>
        <input type="file" name="file"/>
    </p>
    <input type="submit" value="Upload"/>
</form>
</body>
</html>
