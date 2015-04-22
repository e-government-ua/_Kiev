<?php

namespace app\controllers;

use app\models\ProcessDefinitionForm;
use Yii;
use yii\filters\AccessControl;
use yii\web\Controller;
use yii\web\Response;
use yii\web\UploadedFile;

class SiteController extends Controller
{

    public function behaviors()
    {
        return [
            'access' => [
                'class' => AccessControl::className(),
                'only'  => ['definition', 'success', 'upload'],
                'rules' => [
                    [
                        'actions' => ['definition', 'upload', 'success'],
                        'allow'   => true,
                        'roles'   => ['@'],
                    ],
                ],
            ],
        ];
    }

    public function actions()
    {
        return [
            'error' => [
                'class' => 'yii\web\ErrorAction',
            ],
        ];
    }

    public function actionIndex()
    {
        $definitions = Yii::$app->apiClient->getProcessDefinitions();
        return $this->render('index', [
                    'definitions' => $definitions
        ]);
    }

    /**
     *
     * @param string $id
     * @return string
     */
    public function actionDefinition($id)
    {
        $definition = Yii::$app->apiClient->getProcessDefinition($id);
        $model = new ProcessDefinitionForm($definition['formProperties']);

        $attributes = Yii::$app->session->get('definition');
        if ($attributes) {
            $model->setAttributes($attributes);
            Yii::$app->session->remove('definition');
        }

        if ($model->load(Yii::$app->request->post()) && $model->validate()) {
            $model->loadDefaultAtributes();
            if (Yii::$app->user->isTokenExpired()) {
                Yii::$app->user->logout(false);
                Yii::$app->session->set('definition', $model->attributes);
                Yii::$app->user->loginRequired();
            } else {
                $data = Yii::$app->apiClient->saveProcessDefinition($id, $model->attributes);
                Yii::$app->session->setFlash('success', ['id' => $data['id'], 'email' => $model->getFormEmail()]);
                return $this->redirect(['success']);
            }
        }

        return $this->render('definition', [
                    'definition' => $definition,
                    'model'      => $model
        ]);
    }

    /**
     *
     */
    public function actionSuccess()
    {
        if (!Yii::$app->session->hasFlash('success')) {
            return $this->redirect(Yii::$app->homeUrl);
        }
        $data = Yii::$app->session->getFlash('success');
        return $this->render('success', [
                    'data' => $data
        ]);
    }

    /**
     *
     * @return array
     */
    public function actionUpload()
    {
        Yii::$app->response->format = Response::FORMAT_JSON;
        $file = UploadedFile::getInstanceByName('file');

        if ($file->error != UPLOAD_ERR_OK) {
            return ['id' => null, 'name' => 'При завантаженні файлу виникла помилка'];
        }

        $fileData = fopen($file->tempName, 'r');
        $fileId = Yii::$app->apiClient->uploadFile($fileData);


        return ['id' => $fileId, 'name' => $file->name];
    }

}
