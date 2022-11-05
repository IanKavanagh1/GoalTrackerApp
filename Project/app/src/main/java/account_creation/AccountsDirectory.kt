package account_creation

class AccountsDirectory()
{
    private var accounts = listOf<UserAccount>()

    public fun checkAccounts( account: UserAccount) : Boolean
    {
        return accounts.contains(account)
    }

    public fun addAccount(account: UserAccount)
    {
        if( !checkAccounts(account) )
        {
            accounts += account
        }
    }
}