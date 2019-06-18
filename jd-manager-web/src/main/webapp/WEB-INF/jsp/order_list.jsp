<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员登录</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>

    <style>
        body {
            margin-top: 20px;
            margin: 0 auto;
        }

        .carousel-inner .item img {
            width: 100%;
            height: 300px;
        }
    </style>
</head>

<body>

<!--动态包含-->
<jsp:include page="head.jsp"></jsp:include>

<div class="container">
    <div class="row">

        <div style="margin:0 auto; margin-top:10px;width:950px;">
            <strong>我的订单</strong>
            <table class="table table-bordered">
                <c:forEach items="${orderList}" var="order">
                    <tbody>
                    <tr class="success">
                        <th colspan="5">
                            订单编号:${order.orders.oid}&nbsp;&nbsp;&nbsp;&nbsp;
                            状态:
                            <c:if test="${order.orders.state==0}">
                            <a href="${pageContext.request.contextPath}/payOrder/${order.orders.oid}"
                               onclick="return confirm('确认支付${order.orders.total}元吗？')">
                                点击去支付</a>&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <c:if test="${order.orders.state==1}">
                                等待发货&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <c:if test="${order.orders.state==2}">
                                <a href="${pageContext.request.contextPath}/receiveOrder/${order.orders.oid}"
                                onclick="return confirm('确认收货吗？')">
                                    点击确认收货</a>&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <c:if test="${order.orders.state==3}">
                                已完成&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <a href="${pageContext.request.contextPath}/order_info/${order.orders.oid}">查看详情</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;总金额:${order.orders.total}
                        </th>
                    </tr>
                    <tr class="warning">
                        <th>图片</th>
                        <th>商品</th>
                        <th>价格</th>
                        <th>数量</th>
                        <th>小计</th>
                    </tr>
                    <c:forEach items="${order.orderitems}" varStatus="i" var="a">
                        <tr class="active">
                            <td width="60" width="40%">
                                <input type="hidden" name="id" value="${order.orderitems.get(i.count-1).pid}">
                                <img src="${pageContext.request.contextPath}/${order.productList.get(i.count-1).pimage}"
                                     width="70" height="60">
                            </td>
                            <td width="30%">
                                <a href="${pageContext.request.contextPath}/product_info/${order.orderitems.get(i.count-1).pid}">
                                        ${order.productList.get(i.count-1).pname}</a>
                            </td>
                            <td width="20%">
                                ￥${order.productList.get(i.count-1).shopPrice}
                            </td>
                            <td width="10%">
                                    ${order.orderitems.get(i.count-1).count}
                            </td>
                            <td width="15%">
                                <span class="subtotal">￥${order.orderitems.get(i.count-1).subtotal}</span>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </c:forEach>


            </table>
        </div>
    </div>
    <!--分页 -->
    <div style="width:380px;margin:0 auto;margin-top:50px;" align="center">
        <ul class="pagination" style="text-align:center; margin-top:10px;">

            <!--上一页 首页不显示-->
            <c:if test="${curPage>1}">
                <li>
                    <a href="${pageContext.request.contextPath}/order_list/${curPage-1}"
                       aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>
                </li>
            </c:if>

            <!--所有页 前5后4-->
            <c:forEach begin="${curPage-3>0?curPage-3:1}" end="${curPage+3>totalPage?totalPage:curPage+3}" var="i">
                <%--是当前页 不可点--%>
                <c:if test="${curPage==i}">
                    <li class="active"><a href="javascript:void(0)">${i}</a></li>
                </c:if>
                <%--不是当前页 可点--%>
                <c:if test="${curPage!=i}">
                    <li><a href="${pageContext.request.contextPath}/order_list/${i}">${i}</a></li>
                </c:if>
            </c:forEach>

            <!--下一页 末页不显示-->
            <c:if test="${curPage<totalPage}">
                <li>
                    <a href="${pageContext.request.contextPath}/order_list/${curPage+1}"
                       aria-label="Next"><span aria-hidden="true">&raquo;</span></a>
                </li>
            </c:if>


        </ul>
    </div>
    <!-- 分页结束=======================        -->
</div>

<div style="margin-top:50px;">
    <img src="${pageContext.request.contextPath}/image/footer.jpg" width="100%" height="78" alt="我们的优势" title="我们的优势"/>
</div>

<div style="text-align: center;margin-top: 5px;">
    <ul class="list-inline">
        <li><a>关于我们</a></li>
        <li><a>联系我们</a></li>
        <li><a>招贤纳士</a></li>
        <li><a>法律声明</a></li>
        <li><a>友情链接</a></li>
        <li><a target="_blank">支付方式</a></li>
        <li><a target="_blank">配送方式</a></li>
        <li><a>服务声明</a></li>
        <li><a>广告声明</a></li>
    </ul>
</div>
<div style="text-align: center;margin-top: 5px;margin-bottom:20px;">
    Copyright &copy; 2005-2016 传智商城 版权所有
</div>
</body>

</html>