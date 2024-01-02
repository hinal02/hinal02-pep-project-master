package Service;

import DAO.AccountDAO;
import Model.Account;

public class Accountservice {
    AccountDAO accdao;

    public Accountservice() {
        accdao = new AccountDAO();
    }

    public Accountservice(AccountDAO accdao) {
        this.accdao = accdao;
    }

    // create new acc if account is not exist
    public Account newAccount(Account account) {
        if (accdao.geAccountByUserName(account.getUsername()) != null) {
            return null;
        }
        return accdao.insertAccount(account);
    }

    /*
     * check if account is exist with that username and password.
     * If exist return that account.
     */
    public Account authenticateAccount(String username, String password) {
        Account account = accdao.geAccountByUserName(username);
        if (account != null && account.getPassword().equals(password) && account.getUsername().equals(username)) {
            return account;
        }
        return null;

    }

}
