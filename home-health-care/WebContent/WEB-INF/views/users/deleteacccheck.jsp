<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
.btn {
  background-color: #007bff;
  color: white;
  padding: 16px 20px;
  border: none;
  cursor: pointer;
  width: 50%;
  opacity: 0.9;
}
.btn:hover {
  opacity: 1;
}
</style>

<script>
function clicked(e)
{
	if(!confirm('Sei sicuro?')){
		e.preventDefault();
	}
}
</script>
<div style="margin-left:2%">
<c:if test="${not empty errorMessage}">
	<div style="color: red; font-weight: bold; margin: 2%;">${errorMessage}</div>
</c:if>
<div style="display:inline-block"><h3 style="border-bottom:2px solid #007bff">Elimina Profilo</h3></div>
<form name='checkpass' action="<c:url value="/users/checkanddelete" />" method='POST'>
	<table style="border:none">
		<tr>
			<td>Digita nella casella di testo "CONFERMO" per eliminare il tuo account:</td>
			<td><input type='text' name='confirmation' ></td>
		</tr>
		<tr>
			<td><input name="submit" class="btn" type="submit"
				value="Elimina il mio account" onclick="return confirm('Sei sicuro?')" /></td>
		</tr>
	</table>
	</form>
	</div>