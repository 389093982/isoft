package framework

import (
	"encoding/json"
	"fmt"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/core/iworkutil"
	"isoft/isoft_iwork_web/models"
	"strings"
)

type EntityParserNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *EntityParserNode) Execute(trackingId string) {
	inputSchema := this.ParamSchemaCacheParser.GetCacheParamInputSchema()
	for _, item := range inputSchema.ParamInputSchemaItems {
		if strings.HasSuffix(item.ParamName, "_entity") {
			entityName := getEntityNameWithRemovePrefixAndSuffix(item.ParamName)
			// 从 tmpDataMap 中获取入参实体类数据
			entityDataMap := make(map[string]interface{})
			if dataMap, ok := this.TmpDataMap[iworkconst.COMPLEX_PREFIX+entityName+"_data"].([]map[string]interface{}); ok && len(dataMap) > 0 {
				entityDataMap = dataMap[0]
			} else if dataMap, ok := this.TmpDataMap[iworkconst.COMPLEX_PREFIX+entityName+"_data"].(map[string]interface{}); ok {
				entityDataMap = dataMap
			}
			entityFieldStr := this.TmpDataMap[iworkconst.STRING_PREFIX+entityName+"_entity"].(string)
			paramMap := make(map[string]interface{}, 0)
			for _, entityField := range strings.Split(entityFieldStr, ",") {
				// 将数据数据存储到数据中心
				paramMap[fmt.Sprintf("%s.%s", iworkconst.COMPLEX_PREFIX+entityName,
					strings.TrimSpace(entityField))] = entityDataMap[entityField]
			}
			this.DataStore.CacheDatas(this.WorkStep.WorkStepName, paramMap)
		}
	}
}

func (this *EntityParserNode) GetRuntimeParamInputSchema() *iworkmodels.ParamInputSchema {
	var paramMappingsArr []iworkmodels.ParamMapping
	json.Unmarshal([]byte(this.WorkStep.WorkStepParamMapping), &paramMappingsArr)
	items := make([]*iworkmodels.ParamInputSchemaItem, 0)
	for _, paramMapping := range paramMappingsArr {
		// paramMapping 存放实体类定义 $Entity
		items = append(items, &iworkmodels.ParamInputSchemaItem{ParamName: fmt.Sprintf(iworkconst.STRING_PREFIX+"%s_entity", paramMapping)})
		// paramMapping_data 存放实体类数据
		items = append(items, &iworkmodels.ParamInputSchemaItem{ParamName: fmt.Sprintf(iworkconst.COMPLEX_PREFIX+"%s_data", paramMapping)})
	}
	return &iworkmodels.ParamInputSchema{ParamInputSchemaItems: items}
}

func (this *EntityParserNode) GetRuntimeParamOutputSchema() *iworkmodels.ParamOutputSchema {
	items := make([]iworkmodels.ParamOutputSchemaItem, 0)
	inputSchema := this.ParamSchemaCacheParser.GetCacheParamInputSchema()
	for _, item := range inputSchema.ParamInputSchemaItems {
		if !strings.HasSuffix(item.ParamName, "_data") { // _data 需要排除
			// 从用户输入值中提取实体类字段详细信息
			if entityFieldStr := iworkutil.GetParamValueForEntity(this.WorkCache.Work.AppId, item.ParamValue); strings.TrimSpace(entityFieldStr) != "" {
				for _, entityField := range strings.Split(entityFieldStr, ",") {
					// 每个字段放入 items 中
					items = append(items, iworkmodels.ParamOutputSchemaItem{
						ParamName: strings.TrimSpace(entityField), ParentPath: iworkconst.COMPLEX_PREFIX + getEntityNameWithRemovePrefixAndSuffix(item.ParamName),
					})
				}
			}
		}
	}
	return &iworkmodels.ParamOutputSchema{ParamOutputSchemaItems: items}
}

func getEntityNameWithRemovePrefixAndSuffix(paramName string) string {
	// 去除 str_ 和 _entity
	paramName = strings.Replace(paramName, iworkconst.STRING_PREFIX, "", -1)
	paramName = strings.Replace(paramName, "_entity", "", -1)
	return paramName
}
