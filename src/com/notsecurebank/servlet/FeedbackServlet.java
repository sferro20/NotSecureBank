package com.notsecurebank.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.notsecurebank.util.OperationsUtil;
import org.owasp.esapi.ESAPI;

public class FeedbackServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(FeedbackServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doPost");

        // the feedback is not actually submitted
        if (request.getParameter("comments") == null) {
            LOG.error("Null comments.");
            response.sendRedirect("index.jsp");
            return;
        }

        //da errore perchè bisognerebbe importare la libreria.. per questioni di tempo non l'ho fatto (scaricare jar ecc)
        String name = request.getParameter("name");
        name = ESAPI.encoder().encodeForHTML(name);
        if (name != null) {
            request.setAttribute("message_feedback", name);
            String email = request.getParameter("email_addr");
            email = ESAPI.encoder().encodeForHTML(email);
            String subject = request.getParameter("subject");
            subject = ESAPI.encoder().encodeForHTML(subject);
            String comments = request.getParameter("comments");
            comments = ESAPI.encoder().encodeForHTML(comments);
            // store feedback in the DB - display their feedback once submitted

            String feedbackId = OperationsUtil.sendFeedback(name, email, subject, comments);
            if (feedbackId != null) {
                LOG.info("feedbackId = '" + feedbackId + "'.");
                request.setAttribute("feedback_id", feedbackId);
            }

        } else {
            LOG.error("Null name.");
            request.removeAttribute("name");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/feedbacksuccess.jsp");
        dispatcher.forward(request, response);
    }
}
