<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hospira device monitor</title>
    <style>
        .mainTable {
            width: 800px;
            border: 1px solid #778899;
            margin: auto;
        }

        td {
            text-align: left;
        }

        .centeredTxt {
            text-align: center;
        }

        .divBorder {
            border: 1px solid #778899;
            width: 820;
            margin: auto;
            padding-top: 10px;
            padding-bottom: 10px;
        }

        .tableHeaderBG {
            background: #87ceeb;
            border: 2px solid #778899;
        }

        .searchDiv {
            height: 30px;
            text-align: left;
            /*width: 250px;*/
            padding-left: 10px;
            padding-bottom: 10px;
            padding-right: 10px;
        }

        .refreshDiv {
            height: 100%;
            display: inline;
            vertical-align: top;
            width: 100px;
            float: right;
            padding-right: 8px;
        }
    </style>
    <script language="javascript">

        var messagedata;

        window.setInterval(refreshTable, 2000);
        window.onload = refreshTable;

        function refreshTable() {

            var path = window.location.href + "api/messagedata";
            var xhr;
            if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
                xhr = new XMLHttpRequest();
            }
            else {// code for IE6, IE5
                xhr = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xhr.open('GET', path, true);

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status == 200) {
                    messagedata = JSON.parse(xhr.responseText);
                    if (messagedata == null) {
                        alert("No data found");
                    }
                    refreshTime();
                    redrawTable();
                }
            };
            xhr.send(null);
        }

        function refreshTime() {
            var dateTime = getTimeString();

            var timeDiv = document.getElementById("timeRefresh");
            timeDiv.innerHTML = dateTime;
        }

        function getTimeString() {
            var currentdate = new Date();
            return getDateVal(currentdate.getHours()) + ":" + getDateVal(currentdate.getMinutes()) + ":" + getDateVal(currentdate.getSeconds());
        }

        function getDateVal(number) {
            return ((("" + number).length < 2 ? "0" : "") + number)
        }

        function redrawTable() {

            var tbl = document.getElementById("dynamicTable");
            for (var i = tbl.rows.length; i > 1; i--) {
                tbl.deleteRow(i - 1);
            }
                var row = tbl.insertRow(1);
                var cell = row.insertCell(0);
                cell.innerHTML = "Message count";
                cell.width = "20%";

                cell = row.insertCell(1);
                cell.innerHTML = messagedata.count;
                cell.width = "80%";

                row = tbl.insertRow(2);
                cell = row.insertCell(0);
                cell.innerHTML = "Last message";
                cell.width = "20%";

                cell = row.insertCell(1);
                cell.innerHTML = messagedata.lastMessage;
                cell.width = "80%";

//            document.body.appendChild(tbl);
        }

    </script>
</head>
<body onload="refreshTime()">
<div class="centeredTxt">

    <h2>Message monitor</h2>

    <div class="divBorder">

        <div class="searchDiv">

            <div id="timeRefresh" class="refreshDiv"></div>
            <div class="refreshDiv">Last Refreshed:</div>
        </div>

        <div>
            <table id="dynamicTable" class="mainTable">
                <tr class="tableHeaderBG">
                    <td width="20%"></td>
                    <td width="80%">Value</td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>