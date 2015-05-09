<?php
namespace app\tests;
use Yii;

/** Class allows you create session without any requests to bankId oauth providers.
 *  It's use Privat BankId data structure and options
 *  With required user data 
 *  All methods required to repeat requists chain from AuthAction object
 * 
 */
class DevBankIdAuth extends \app\components\BankId
{
    protected $unicId; 
    
    public function __construct($config = array())
    {        
        parent::__construct($config);
        $this->unicId = substr(uniqid(), 0, 11);
    }
    
    /** Create Auth Url because it's required by AuthAction
     *  and redirect to return Url with code param
     * 
     * @param array $params
     * @return Redirect
     */
    public function buildAuthUrl(array $params = [])
    {
        return Yii::$app->getResponse()->redirect($this->getReturnUrl() . '&code='. $this->unicId);
    }

    /** return dummy used data 
     * 
     * @return array
     */
    public function initUserAttributes()
    {
        return [
            "state"=> "ok",
            "customer" => [
                "type" => "physical",
                "clId" => "3e9a720b0f23347345b3cdcb2cdfc". $this->unicId,
                "lastName" => "Пхпович_". $this->unicId,
                "firstName" => "Борис_" . $this->unicId,
                "middleName" =>  "Петрович_" . $this->unicId,
                "phone"=> "+380500000000",
                "birthDay"=> rand(1,30) . "." . rand(1,12) . "." . rand(1950, 2000),
                "inn"=> "287261" . rand(1000, 9999),
                "addresses" => [
                    [
                        "type" => "factual",
                        "country" => "UA",
                        "state"=> "Kiev",
                        "city"=> "Kiev",
                        "street"=> "Khreschatyk",
                        "houseNo"=> "11",
                        "flatNo"=> "12"
                    ]
                 ],
                "documents" => [
                    [
                        "type" => "passport",
                        "series"=> "HHM",
                        "number"=> rand(100000, 999999),
                        "issue"=> "issued by text ",
                        "dateIssue"=> "15.06." . rand(2000, 2015),
                        "issueCountryIso2" => "UA",
                    ]
                ],
                "scans" => [
                    [
                        "type" => "passport",
                        "link" => "https://bankid.privatbank.ua/ResourceService/checked/scan/passport" ,
                        "dateCreate" => "15.06." . rand(2000, 2015),                         
                    ]
                ]
            ]
        ];
    }    
    
    /** method call required by AuthAction
     *  so it's return dummy data
     * 
     * @param array $params
     * @return Redirect
     */    
    public function fetchAccessToken($authCode, array $params = [])
    {
        $response = [
            "access_token" => "eae6473c-0aa8-4436-98b2-c". $this->unicId, 
            "token_type"    => "bearer",
            "refresh_token" => "a9b54a8e-40d9-46ae-9adb-9". $this->unicId,
            "expires_in" => 179,
            "scope"  => "read trust write"   
        ];
        
        $token = $this->createToken(['params' => $response]);
        $this->setAccessToken($token);
        return $token;
    }   

    
}