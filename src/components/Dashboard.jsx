import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

// Axios instance with JWT token interceptor
const api = axios.create({ baseURL: 'http://localhost:8080/api/account' });

// Attach JWT token to all requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

function Dashboard() {
  const [balance, setBalance] = useState(0);
  const [depositAmount, setDepositAmount] = useState('');
  const [withdrawAmount, setWithdrawAmount] = useState('');
  const [transferAmount, setTransferAmount] = useState('');
  const [toUsername, setToUsername] = useState('');
  const [actionMessage, setActionMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  // Check for token and fetch balance
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/');
      return;
    }
    fetchBalance();
  }, [navigate]);

  const fetchBalance = async () => {
    try {
      setIsLoading(true);
      const response = await api.get('/balance');
      setBalance(response.data);
    } catch (error) {
      handleRequestError(error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRequestError = (error) => {
    const message = error.response?.data?.message || 'Something went wrong. Please try again.';
    alert(message);
  };

  // Deposit
  const handleDeposit = async () => {
    if (!depositAmount || isNaN(depositAmount) || depositAmount <= 0) {
      return alert('Please enter a valid deposit amount');
    }
    try {
      setIsLoading(true);
      await api.post('/deposit', { amount: parseFloat(depositAmount) });
      setActionMessage('Deposit successful');
      setDepositAmount('');
      fetchBalance();
    } catch (error) {
      handleRequestError(error);
    } finally {
      setIsLoading(false);
    }
  };

  // Withdraw
  const handleWithdraw = async () => {
    if (!withdrawAmount || isNaN(withdrawAmount) || withdrawAmount <= 0) {
      return alert('Please enter a valid withdraw amount');
    }
    try {
      setIsLoading(true);
      await api.post('/withdraw', { amount: parseFloat(withdrawAmount) });
      setActionMessage('Withdraw successful');
      setWithdrawAmount('');
      fetchBalance();
    } catch (error) {
      handleRequestError(error);
    } finally {
      setIsLoading(false);
    }
  };

  // Transfer
  const handleTransfer = async () => {
    if (!transferAmount || isNaN(transferAmount) || transferAmount <= 0) {
      return alert('Please enter a valid transfer amount');
    }
    if (!toUsername) return alert('Please enter a recipient username');
    try {
      setIsLoading(true);
      await api.post('/transfer', { toUsername, amount: parseFloat(transferAmount) });
      setActionMessage(`Transfer to ${toUsername} successful`);
      setTransferAmount('');
      setToUsername('');
      fetchBalance();
    } catch (error) {
      handleRequestError(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="w-screen h-screen bg-gradient-to-br from-indigo-900 to-purple-800 flex items-center justify-center p-4">
      <div className="w-full max-w-2xl">
        <div className="bg-white/10 backdrop-blur-md rounded-3xl shadow-2xl overflow-hidden border border-white/20">
          <div className="p-10">
            {/* Header */}
            <div className="flex justify-between items-center mb-10">
              <h2 className="text-3xl font-bold text-white">Banking Dashboard</h2>
              <button
                onClick={() => {
                  localStorage.removeItem('token');
                  localStorage.removeItem('refreshToken');
                  navigate('/');
                }}
                className="px-5 py-3 bg-red-500/90 text-white rounded-xl hover:bg-red-600 transition-colors"
              >
                Logout
              </button>
            </div>

            {/* Balance Display */}
            <div className="bg-white/5 p-8 rounded-xl mb-10 border border-white/10">
              <h3 className="text-lg font-medium text-white/80 mb-3">Account Balance</h3>
              <p className="text-4xl font-bold text-white">${balance.toFixed(2)}</p>
            </div>

            {/* Deposit & Withdraw */}
            <div className="space-y-10">
              <div className="space-y-6">
                <h3 className="text-xl font-medium text-white">Deposit / Withdraw</h3>

                {/* Deposit Row */}
                <div className="flex flex-col sm:flex-row sm:space-x-4 space-y-4 sm:space-y-0">
                  <input
                    type="number"
                    placeholder="Deposit Amount"
                    value={depositAmount}
                    onChange={(e) => setDepositAmount(e.target.value)}
                    className="flex-1 px-5 py-3 bg-white/5 text-white rounded-xl border border-white/20 focus:bg-white/10 focus:border-white/40 focus:ring-2 focus:ring-white/30 outline-none transition-all placeholder:text-white/50"
                  />
                  <button
                    onClick={handleDeposit}
                    disabled={isLoading}
                    className={`px-6 py-3 bg-green-500/90 text-white rounded-xl hover:bg-green-600 transition-colors ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                  >
                    {isLoading ? 'Processing...' : 'Deposit'}
                  </button>
                </div>

                {/* Withdraw Row */}
                <div className="flex flex-col sm:flex-row sm:space-x-4 space-y-4 sm:space-y-0">
                  <input
                    type="number"
                    placeholder="Withdraw Amount"
                    value={withdrawAmount}
                    onChange={(e) => setWithdrawAmount(e.target.value)}
                    className="flex-1 px-5 py-3 bg-white/5 text-white rounded-xl border border-white/20 focus:bg-white/10 focus:border-white/40 focus:ring-2 focus:ring-white/30 outline-none transition-all placeholder:text-white/50"
                  />
                  <button
                    onClick={handleWithdraw}
                    disabled={isLoading}
                    className={`px-6 py-3 bg-yellow-500/90 text-white rounded-xl hover:bg-yellow-600 transition-colors ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                  >
                    {isLoading ? 'Processing...' : 'Withdraw'}
                  </button>
                </div>
              </div>

              {/* Transfer */}
              <div className="space-y-6">
                <h3 className="text-xl font-medium text-white">Transfer Money</h3>
                <div className="flex flex-col sm:flex-row sm:space-x-4 space-y-4 sm:space-y-0">
                  <input
                    type="text"
                    placeholder="Recipient Username"
                    value={toUsername}
                    onChange={(e) => setToUsername(e.target.value)}
                    className="flex-1 px-5 py-3 bg-white/5 text-white rounded-xl border border-white/20 focus:bg-white/10 focus:border-white/40 focus:ring-2 focus:ring-white/30 outline-none transition-all placeholder:text-white/50"
                  />
                  <input
                    type="number"
                    placeholder="Amount"
                    value={transferAmount}
                    onChange={(e) => setTransferAmount(e.target.value)}
                    className="flex-1 px-5 py-3 bg-white/5 text-white rounded-xl border border-white/20 focus:bg-white/10 focus:border-white/40 focus:ring-2 focus:ring-white/30 outline-none transition-all placeholder:text-white/50"
                  />
                  <button
                    onClick={handleTransfer}
                    disabled={isLoading}
                    className={`px-6 py-3 bg-purple-500/90 text-white rounded-xl hover:bg-purple-600 transition-colors ${isLoading ? 'opacity-70 cursor-not-allowed' : ''}`}
                  >
                    {isLoading ? 'Processing...' : 'Transfer'}
                  </button>
                </div>
              </div>
            </div>

            {/* Action Message */}
            {actionMessage && (
              <div className="mt-8 p-4 bg-green-500/20 border border-green-400 text-white rounded-xl">
                {actionMessage}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
