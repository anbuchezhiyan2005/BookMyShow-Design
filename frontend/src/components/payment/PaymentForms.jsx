import { useState } from 'react';
import './PaymentForms.css';

export const UpiPaymentForm = ({ onSubmit, loading }) => {
  const [upiId, setUpiId] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ upiId });
  };

  return (
    <form onSubmit={handleSubmit} className="payment-form">
      <div className="form-group">
        <label>UPI ID</label>
        <input
          type="text"
          value={upiId}
          onChange={(e) => setUpiId(e.target.value)}
          placeholder="example@upi"
          required
          pattern=".*@.*"
        />
      </div>
      <button type="submit" disabled={loading} className="btn-primary">
        {loading ? 'Processing...' : 'Pay Now'}
      </button>
    </form>
  );
};

export const CardPaymentForm = ({ onSubmit, loading }) => {
  const [cardNumber, setCardNumber] = useState('');
  const [cvv, setCvv] = useState('');
  const [expiry, setExpiry] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ cardNumber, cvv, expiry });
  };

  return (
    <form onSubmit={handleSubmit} className="payment-form">
      <div className="form-group">
        <label>Card Number</label>
        <input
          type="text"
          value={cardNumber}
          onChange={(e) => setCardNumber(e.target.value.replace(/\D/g, '').slice(0, 16))}
          placeholder="1234 5678 9012 3456"
          required
          minLength="16"
          maxLength="16"
        />
      </div>
      <div className="form-row">
        <div className="form-group">
          <label>CVV</label>
          <input
            type="text"
            value={cvv}
            onChange={(e) => setCvv(e.target.value.replace(/\D/g, '').slice(0, 3))}
            placeholder="123"
            required
            minLength="3"
            maxLength="3"
          />
        </div>
        <div className="form-group">
          <label>Expiry</label>
          <input
            type="text"
            value={expiry}
            onChange={(e) => setExpiry(e.target.value)}
            placeholder="MM/YY"
            required
          />
        </div>
      </div>
      <button type="submit" disabled={loading} className="btn-primary">
        {loading ? 'Processing...' : 'Pay Now'}
      </button>
    </form>
  );
};

export const NetBankingForm = ({ onSubmit, loading }) => {
  const [bankName, setBankName] = useState('');
  const [accountNumber, setAccountNumber] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ bankName, accountNumber });
  };

  return (
    <form onSubmit={handleSubmit} className="payment-form">
      <div className="form-group">
        <label>Bank Name</label>
        <select
          value={bankName}
          onChange={(e) => setBankName(e.target.value)}
          required
        >
          <option value="">Select Bank</option>
          <option value="SBI">State Bank of India</option>
          <option value="HDFC">HDFC Bank</option>
          <option value="ICICI">ICICI Bank</option>
          <option value="Axis">Axis Bank</option>
          <option value="PNB">Punjab National Bank</option>
        </select>
      </div>
      <div className="form-group">
        <label>Account Number</label>
        <input
          type="text"
          value={accountNumber}
          onChange={(e) => setAccountNumber(e.target.value.replace(/\D/g, ''))}
          placeholder="Enter account number"
          required
        />
      </div>
      <button type="submit" disabled={loading} className="btn-primary">
        {loading ? 'Processing...' : 'Pay Now'}
      </button>
    </form>
  );
};
