<?php

$params = require(__DIR__ . '/params.php');

$config = [
    'id'             => 'e-government-ua',
    'name'           => 'Портал -= Электронное правительство =-',
    'timeZone'       => 'Europe/Kiev',
    'language'       => 'uk',
    'sourceLanguage' => 'uk',
    'basePath'       => dirname(__DIR__),
    'bootstrap'      => ['log'],
    'components'     => [
        'authClientCollection' => [
            'class'   => 'yii\authclient\Collection',
            'clients' => [
                'bankid' => [
                    'class'        => 'app\components\BankId',
                    'clientId'     => 'dniprorada',
                    'clientSecret' => 'NzVmYTI5NGJjMDg3OThlYjljNDY5YjYxYjJiMjJhNA==',
                ],
            ],
        ],
        'apiClient'            => [
            'class'  => 'app\components\ApiClient',
            'apiUrl' => 'https://52.17.126.64:8080/wf-dniprorada/service/',
            'login' => 'activiti-master',
            'password' => 'UjhtJnEvf!'
        ],
        'request'              => [
            // !!! insert a secret key in the following (if it is empty) - this is required by cookie validation
            'cookieValidationKey' => 'SEHDM5JmdUCQPO1lulF3kEFQ44_EnxGb',
        ],
        'urlManager'           => [
            'enablePrettyUrl'     => true,
            'enableStrictParsing' => false,
            'showScriptName'      => false,
            'rules'               => [
            ],
        ],
        'cache'                => [
            'class' => 'yii\caching\FileCache',
        ],
        'user'                 => [
            'class'           => 'app\components\User',
            'identityClass'   => 'app\models\User',
            'enableAutoLogin' => false,
            'authTimeout'     => 3600,
            'loginUrl'        => ['/auth/login']
        ],
        'session'              => [
            'class' => 'yii\web\Session',
        ],
        'errorHandler'         => [
            'errorAction' => 'site/error',
        ],
        'mailer'               => [
            'class'            => 'yii\swiftmailer\Mailer',
            'useFileTransport' => false,
        ],
        'log'                  => [
            'traceLevel' => YII_DEBUG ? 3 : 0,
            'targets'    => [
                [
                    'class'  => 'yii\log\FileTarget',
                    'levels' => ['error', 'warning'],
                ],
                [
                    'class'   => 'yii\log\EmailTarget',
                    'mailer'  => 'mailer',
                    'levels'  => ['error', 'warning'],
                    'except'  => [
                        'yii\web\HttpException:404',
                        'yii\web\HttpException:400',
                        'yii\debug\Module::checkAccess'
                    ],
                    'message' => [
                        'from'    => 'error@e-gov.loveitgroup.com.ua',
                        'to'      => ['rak.kture@gmail.com'],
                        'subject' => 'Application Log',
                    ],
                ],
            ],
        ],
        'db'                   => require(__DIR__ . '/db.php'),
    ],
    'params'         => $params,
];

if (YII_ENV_DEV) {
    // configuration adjustments for 'dev' environment
    $config['bootstrap'][] = 'debug';
    $config['modules']['debug'] = [
        'class'      => 'yii\debug\Module',
        'allowedIPs' => ['77.120.187.109', '93.76.203.247', '77.120.188.133']
    ];

    $config['bootstrap'][] = 'gii';
    $config['modules']['gii'] = 'yii\gii\Module';
}

return $config;
