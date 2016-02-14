<%--
  Created by IntelliJ IDEA.
  User: Rakib
  Date: 23-Jan-16
  Time: 10:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Convex Hull</title>
</head>
<body bgcolor="#cccccc">
<h1><a href="/">Convex Hull</a></h1>

<p style="color: red;">
    <%
        out.write(
                request.getAttribute("errorMessage") == null ? "" : request.getAttribute("errorMessage").toString()
        );
    %>
</p>

<form action="" method="post" enctype="multipart/form-data">
    Image: <input type="file" name="imageFile"/>

    Select Color: <input type="text" id="color" name="color" value="${color}"/>

    Color Tolerance: <input type="text" name="tolerance" value="${tolerance}"/>

    <input type="submit" value="Update"/>
</form>

<br/>
<br/>

<table border="1" width="100%">
    <tr>
        <th>Input</th>
        <th>Output</th>
    </tr>

    <tr>
        <td>
            <img id="originalImage" src="${inputImage}" style="max-width: 100%; display: none;" alt="Input"/>
            <canvas id="myCanvas" style="border:1px solid #c3c3c3;">
                Your browser does not support the canvas element.
            </canvas>
        </td>
        <td>
            <img src="${outputImage}" style="max-width: 100%;" alt="Output"/>
        </td>
    </tr>
</table>

<script src="../Group29_P57/resources/jquery.min.js"></script>

<script type="text/javascript">
    window.onload = function () {
        var canvas = document.getElementById('myCanvas');
        var originalImage = document.getElementById('originalImage');
        canvas.width = originalImage.width;
        canvas.height = originalImage.height;
        var context = canvas.getContext('2d');
        var img = new Image();
        img.src = `${inputImage}`;
        img.alt = 'Input';
        img.style.maxWidth = '100%';
        context.drawImage(img, 0, 0);
    };

    function findPos(obj) {
        var current_left = 0, current_top = 0;
        if (obj.offsetParent) {
            do {
                current_left += obj.offsetLeft;
                current_top += obj.offsetTop;
            } while (obj = obj.offsetParent);
            return {x: current_left, y: current_top};
        }
        return undefined;
    }

    function rgbToHex(r, g, b) {
        if (r > 255 || g > 255 || b > 255)
            throw "Invalid color component";
        return ((r << 16) | (g << 8) | b).toString(16);
    }

    $('#myCanvas').click(function (e) {
        var position = findPos(this);
        var x = e.pageX - position.x;
        var y = e.pageY - position.y;
        var coordinate = "x=" + x + ", y=" + y;
        var canvas = this.getContext('2d');
        var p = canvas.getImageData(x, y, 1, 1).data;
        var hex = "#" + ("000000" + rgbToHex(p[0], p[1], p[2])).slice(-6);
        $('#color').val(hex);
        //alert("HEX: " + hex);
    });
</script>
</body>
</html>
