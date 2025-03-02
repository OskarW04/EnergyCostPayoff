import { useState, useEffect } from "react";
import { API } from "../../utils/Api";

function Payments() {
    const [payments, setPayments] = useState([]);
    const [selectedPaymentId, setSelectedPaymentId] = useState("");

    useEffect(() => {
        const fetchPayments = async () => {
            const response = await API.get("/api/billing/manager/getAll");
            setPayments(response.data);
        };

        fetchPayments();
    }, []);

    const handleDeletePayment = async (paymentId) => {
        try {
            await API.delete(`/api/billing/manager/delete?paymentId=${paymentId}`);
            alert("Payment deleted successfully!");
            setPayments(payments.filter((payment) => payment.id !== paymentId));
        } catch (error) {
            console.error("error: ", error.request.response)
            alert("Error deleting payment");
        }
    };

    return (
        <div>
            <h1>Payments</h1>
            <div>
                <h2>All Payments</h2>
                {payments.map((payment) => (
                    <div key={payment.id}>
                        <p>Payment ID: {payment.id}</p>
                        <p>Amount: {payment.amount}</p>
                        <p>Date: {payment.paymentDate}</p>
                        <p>Paid: {payment.payed ? "Yes" : "No"}</p>
                        <button onClick={() => handleDeletePayment(payment.id)}>Delete</button>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Payments;