<%-- SiteMesh has a bug where error pages aren't decorated - hence the full HTML --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%@ include file="/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>A página não pode ser exibida.</title>
    <meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <link rel="shortcut icon" href="${ctx}/images/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/styles/deliciouslyblue/theme.css" title="default" />
    <link rel="alternate stylesheet" type="text/css" href="${ctx}/styles/deliciouslygreen/theme.css" title="green" />
    <script type="text/javascript" src="${ctx}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/scriptaculous.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/stylesheetswitcher.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/global.js"></script>
</head>
<body>
<a name="top"></a>
<div id="page">

      <div id="header" class="clearfix" >
              <a href="" title="WikiCrimes - Mapeando Crimes Colaborativamente"> 
                   <img src="${ctx}/images/wikicrimes_pt.jpg" alt="WikiCrime" /></a> 
    </div>

    <div id="content">

        <div id="main">
            <h3>Desculpe. A página não pode ser exibida devido a problemas internos. Tente Novamente</h3>
            
            <input type="button" class="button" value="Voltar para o mapa"
            		onclick="location.href='${ctx}/main.html'"/>
            
            <p style="text-align: center; margin-top: 20px">
                <img style="border: 0" src="<c:url value="/images/404.jpg"/>" alt="Emerald Lake - Western Canada" />
            </p>
        </div>
        
       

    </div><!-- end content -->

    <div id="footer">
        <p>
            <a href="">link</a> |
            <a href="">link</a> |
                        <a href="">link</a> |

        </p>
    </div>
</div>
</body>
</html>
