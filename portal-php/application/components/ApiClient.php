<?php

namespace app\components;

use GuzzleHttp\Client as HttpClient;
use GuzzleHttp\Post\PostFile;
use Yii;
use yii\base\Component;
use yii\base\InvalidConfigException;
use yii\helpers\Json;

class ApiClient extends Component
{

    /**
     * @var string
     */
    public $apiUrl;

    /**
     * @var string
     */
    public $login;

    /**
     * @var string
     */
    public $password;

    /**
     * @var HttpClient a client to make requests to the API
     */
    private $_guzzle;

    /**
     *
     * @throws InvalidConfigException
     */
    public function init()
    {
        if (empty($this->apiUrl)) {
            throw new InvalidConfigException('Необходимо заполнить параметр apiUrl.');
        }
    }

    /**
     *
     * @return array
     */
    public function getProcessDefinitions()
    {
        $client = $this->getGuzzleClient();
        $response = $client->get('repository/process-definitions?latest=true');
        $result = $response->json();
        return $result['data'];
    }

    /**
     *
     * @param string $id
     * @return array
     */
    public function getProcessDefinition($id)
    {
        $client = $this->getGuzzleClient();
        $response = $client->get('form/form-data', ['query' => ['processDefinitionId' => $id]]);
        $result = $response->json();
        return $result;
    }

    /**
     *
     * @param string $id
     * @param array $properties
     * @return array
     */
    public function saveProcessDefinition($id, $properties)
    {
        $user = Yii::$app->user->loadUser();

        $props = [
            [
                'id' => 'processName',
                'value' => ''
            ]
        ];


        foreach ($properties as $key => $value) {
            $props[] = ['id' => $key, 'value' => $value];
        }
        $data = Json::encode([
                    'processDefinitionId' => $id,
                    'businessKey'         => $user->authKey,
                    'properties'          => $props
        ]);

        $client = $this->getGuzzleClient();
        $response = $client->post('form/form-data', [
            'headers' => ['Content-Type' => 'application/json;charset="utf-8"', 'Accept' => '*'],
            'body'    => $data
        ]);
        $result = $response->json();
        return $result;
    }

    /**
     *
     * @param string $fileName
     * @param string $fileData
     * @return string
     */
    public function uploadFile($fileName, $fileData)
    {
        $client = $this->getGuzzleClient();
        $response = $client->post('rest/file/upload_file_to_redis', [
            'body'    => [
                'file' => new PostFile('file', $fileData, $fileName)
            ]
        ]);
        $result = $response->getBody()->__toString();
        return $result;
    }

    /**
     * Returns the guzzle client
     * @return HttpClient
     */
    protected function getGuzzleClient()
    {
        if ($this->_guzzle == null) {
            $this->_guzzle = new HttpClient([
                'base_url' => $this->apiUrl,
                'defaults' => [
                    'verify' => false,
                    'auth'   => [
                        $this->login,
                        $this->password
                    ],
                ]
            ]);
        }
        return $this->_guzzle;
    }

}
