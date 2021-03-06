/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package duytt.controller;

import duytt.daos.QuizDAO;
import duytt.daos.SubjectDAO;
import duytt.dtos.History;
import duytt.dtos.Subject;
import duytt.dtos.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author thant
 */
public class View_History_Controller extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	private final static String MORE = "history.jsp";

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try {
			HttpSession session = request.getSession();
			session.setAttribute("SUCCESS", null);
			User user = (User) session.getAttribute("USER");
			String email = user.getEmail();
			boolean check = true;
			String index = request.getParameter("index");
			if (index == null) {
				index = "1";
			} else if (!index.matches("[0-9]+") || index.isEmpty() || index.equals("")) {
				check = false;
				request.setAttribute("INDEX_ERROR", "Please Search Again");
			}
			List<Subject> listSub = new SubjectDAO().getSubject();
			String subId = request.getParameter("sub");
			if (subId == null) {
				subId = listSub.get(0).getSubId();
			}
			if (subId.isEmpty() || subId.equals("")) {
				check = false;
				request.setAttribute("SUBID_ERROR", "Please Choose again SUBJECT");

			}

			if (check) {
				List<History> list = new QuizDAO().getHistory(subId, email, Integer.parseInt(index));
				int soluong = new QuizDAO().countHistory(subId, email);
				int sopage = soluong / 20;
				if (soluong % 20 != 0) {
					sopage++;
				}
				request.setAttribute("LIST", list);
				session.setAttribute("SOPAGE", sopage);
				request.setAttribute("INDEX", index);
				session.setAttribute("SUBID", subId);
				session.setAttribute("LISTSUB", listSub);
			}
		} catch (Exception e) {
			log("More_Controller: " + e.toString());
		} finally {
			request.getRequestDispatcher(MORE).forward(request, response);
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
