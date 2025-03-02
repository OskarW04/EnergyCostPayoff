import { useState, useEffect } from "react";
import { API } from "../../utils/Api";

function PaymentsTenant() {
    const [currentPayments, setCurrentPayments] = useState([]);
    const [paymentHistory, setPaymentHistory] = useState([]);

    useEffect(() => {
        const fetchPayments = async () => {
            try {
                const currentResponse = await API.get("/api/billing/tenant/currentPayments");
                setCurrentPayments(currentResponse.data);

                const historyResponse = await API.get("/api/billing/tenant/paymentHistory");
                setPaymentHistory(historyResponse.data);
            } catch (error) {
                console.error("Error fetching payments:", error.request.response);
                alert("Failed to fetch payments.");
            }
        };

        fetchPayments();
    }, []);

    const handlePay = async (paymentId) => {
        try {
            await API.post(`/api/billing/tenant/pay`, null, {
                params: { paymentId },
            });
            alert("Payment successfully processed!");
            setCurrentPayments(currentPayments.filter((payment) => payment.id !== paymentId));
        } catch (error) {
            console.error("Error processing payment:", error.request.response);
            alert("Failed to process payment.");
        }
    };

    return (
        <div>
            <h1>Payments</h1>
            <div>
                <h2>Current Payments</h2>
                {currentPayments.length === 0 ? (
                    <p>No current payments.</p>
                ) : (
                    currentPayments.map((payment) => (
                        <div
                            key={payment.id}
                            style={{ border: "1px solid #ddd", padding: "10px", marginBottom: "10px" }}
                        >
                            <p>Payment ID: {payment.id}</p>
                            <p>Amount: {payment.amount}</p>
                            <p>Date: {payment.paymentDate}</p>
                            <p>Paid: {payment.payed ? "Yes" : "No"}</p>
                            <button onClick={() => handlePay(payment.id)}>Pay</button>
                        </div>
                    ))
                )}
            </div>
            <div>
                <h2>Payment History</h2>
                {paymentHistory.length === 0 ? (
                    <p>No payment history.</p>
                ) : (
                    paymentHistory.map((payment) => (
                        <div
                            key={payment.id}
                            style={{ border: "1px solid #ddd", padding: "10px", marginBottom: "10px" }}
                        >
                            <p>Payment ID: {payment.id}</p>
                            <p>Amount: {payment.amount}</p>
                            <p>Date: {payment.paymentDate}</p>
                            <p>Paid: {payment.payed ? "Yes" : "No"}</p>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}

export default PaymentsTenant;
