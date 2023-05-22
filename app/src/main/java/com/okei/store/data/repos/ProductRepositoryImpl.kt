package com.okei.store.data.repos

import com.okei.store.R
import com.okei.store.data.data_source.api.server.ProductApi
import com.okei.store.domain.model.error.AppError
import com.okei.store.domain.model.product.ProductModel
import com.okei.store.domain.repos.ProductRepository
import kotlinx.coroutines.delay
import java.net.UnknownHostException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
) : ProductRepository {
    override suspend fun getProducts(): List<ProductModel> {
        try {
            return productApi.getProducts()
        }catch (e: UnknownHostException){
            throw AppError.NoNetworkAccess
        }
//        delay(1_000L)
//        return listOf(
//            object : ProductModel{
//                override val id = "1"
//                override val name = "Сувенир новогодний"
//                override val mainImageUrl =
//                    "android.resource://com.okei.store/${R.drawable.image_suv_1}"
//                override val listImageUrl = listOf(
//                    "android.resource://com.okei.store/${R.drawable.image_suv_1}",
//                    "android.resource://com.okei.store/${R.drawable.image_suv_2}"
//                )
//                override val description = ""
//                override val price  = 500
//                override val quantityInStock = 2
//
//            },
//            object : ProductModel{
//                override val id = "2"
//                override val name = "Механический глобус деревянный"
//                override val mainImageUrl=
//                    "android.resource://com.okei.store/${R.drawable.image_glob_1}"
//                override val listImageUrl = listOf(
//                    "android.resource://com.okei.store/${R.drawable.image_glob_1}",
//                    "android.resource://com.okei.store/${R.drawable.image_glob_2}",
//                    "android.resource://com.okei.store/${R.drawable.image_glob_3}",
//                )
//                override val description = ""
//                override val price = 1500
//                override val quantityInStock = 20
//
//            },
//            object : ProductModel{
//                override val id = "3"
//                override val name = "Cувенир пасхальный кролик"
//                override val mainImageUrl=
//                    "android.resource://com.okei.store/${R.drawable.image_reb_1}"
//                override val listImageUrl = listOf(
//                    "android.resource://com.okei.store/${R.drawable.image_reb_1}",
//                    "android.resource://com.okei.store/${R.drawable.image_reb_2}",
//                )
//                override val description = ""
//                override val price = 300
//                override val quantityInStock = 5
//
//            },
//            object : ProductModel{
//                override val id = "4"
//                override val name = "Cалфетница подарок на восьмое марта"
//                override val mainImageUrl=
//                    "android.resource://com.okei.store/${R.drawable.image_sal_1}"
//                override val listImageUrl = listOf(
//                    "android.resource://com.okei.store/${R.drawable.image_sal_1}",
//                    "android.resource://com.okei.store/${R.drawable.image_sal_2}",
//                )
//                override val description = ""
//                override val price = 800
//                override val quantityInStock = 5
//            },
//            object : ProductModel{
//                override val id = "5"
//                override val name = "Cувенир дискета деревянный"
//                override val mainImageUrl=
//                    "android.resource://com.okei.store/${R.drawable.image_dis_1}"
//                override val listImageUrl = listOf(
//                    "android.resource://com.okei.store/${R.drawable.image_dis_1}",
//                    "android.resource://com.okei.store/${R.drawable.image_dis_2}",
//                )
//                override val description = ""
//                override val price = 500
//                override val quantityInStock = 5
//            },
//            object : ProductModel{
//                override val id = "FFA0-CCD2-4738-109B"
//                override val name = "Деревянный самолет"
//                override val mainImageUrl = "android.resource://com.okei.store/${R.drawable.image_airplane_3}"
//                override val listImageUrl = listOf(
//                    mainImageUrl,
//                    "android.resource://com.okei.store/${R.drawable.image_airplane_2}",
//                    "android.resource://com.okei.store/${R.drawable.image_airplane_1}"
//                )
//                override val description = """
//                    Все детали выполнены из качественной древесины и имеют сильный насыщенный запах дерева. Наборы укомплектованы клеем ПВА и наждачной бумагой. Любой конструктор после сборки можно раскрасить акриловыми и гуашевыми красками, но даже без этого в результате получается полноценная красивая игрушка с подвижными элементами.
//
//                    Тип         Деревянный
//                    Серии       Техника
//                    Размеры,    мм 365х240х95
//                    Длина модели, см    37
//                    Вес товара, г   320
//                """.trimIndent()
//                override val price = 1000
//                override val quantityInStock = 30
//            },
//            object : ProductModel{
//                override val id = "3F9A-00CC-BBC2-FC83"
//                override val name = "Деревянная подставка для декоративных цветов"
//                override val mainImageUrl = "android.resource://com.okei.store/${R.drawable.image_pod_1}"
//                override val listImageUrl = listOf(
//                    mainImageUrl,
//                    "android.resource://com.okei.store/${R.drawable.image_pod_2}",
//                    "android.resource://com.okei.store/${R.drawable.image_pod_3}",
//                )
//                override val description = """
//                    Изящные формы подставки и богатая текстура натуральной фанерки под светло-бежевый покрытием акцентирует внимание на растении и станет благородным дополнением в интерьере.
//
//                    Вес, кг     200
//                    Размер (Д х Ш х В) (мм)     500х500х3
//                    Площадь (м²)     0.25
//                    Ширина (см) 50
//                    Длина (см)  50
//                    Толщина (мм)    3
//                    Дополнительно   2 искусственных цветочка
//                """.trimIndent()
//                override val price = 600
//                override val quantityInStock = 15
//
//            }
//        )
    }
}