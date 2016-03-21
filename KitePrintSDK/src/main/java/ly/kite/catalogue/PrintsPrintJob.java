package ly.kite.catalogue;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by deonbotha on 09/02/2014.
 */
class PrintsPrintJob extends PrintJob {

    private static final long serialVersionUID = 1L;
    private List<Asset> mAssetList;


  public PrintsPrintJob( Product product, HashMap<String,String> optionMap, List<Asset> assetList )
    {
    super( product, optionMap );

    mAssetList = assetList;
    }

  public PrintsPrintJob( Product product, List<Asset> assetList )
    {
    this( product, null, assetList );
    }


    @Override
    public BigDecimal getCost(String currencyCode) {
        Product product = getProduct();
        BigDecimal sheetCost = product.getCost(currencyCode);
        int expectedQuantity = product.getQuantityPerSheet();

        int numOrders = (int) Math.floor((getQuantity() + expectedQuantity - 1) / expectedQuantity);
        return sheetCost.multiply(new BigDecimal(numOrders));
    }

    @Override
    public Set<String> getCurrenciesSupported() {
        try
            {
            Product product = getProduct();

            return product.getCurrenciesSupported();
            }
        catch ( IllegalArgumentException iae )
            {
            // Fall through
            }

    return Collections.EMPTY_SET;
    }

    @Override
    public int getQuantity() {
        return mAssetList.size();
    }

    @Override
    List<Asset> getAssetsForUploading() {
        return mAssetList;
    }


  @Override
  JSONObject getJSONRepresentation()
    {
    JSONObject jsonObject = new JSONObject();

    try
      {
      jsonObject.put( "template_id", getProductId() );

      addProductOptions( jsonObject );

      putAssetsJSON( mAssetList, jsonObject );

      jsonObject.put( "frame_contents", new JSONObject() );
      }
    catch ( JSONException ex )
      {
      throw ( new RuntimeException( ex ) ); // this should NEVER happen :)
      }

    return ( jsonObject );
    }


  /*****************************************************
   *
   * Adds the assets to the supplied JSON object. The default
   * implementation just adds the assets as an array.
   *
   *****************************************************/
  protected void putAssetsJSON( List<Asset> assetList, JSONObject jsonObject ) throws JSONException
    {
    JSONArray assetsJSONArray = new JSONArray();

    for ( Asset asset : assetList )
      {
      assetsJSONArray.put( "" + asset.getId() );
      }

    jsonObject.put( "assets", assetsJSONArray );
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel( parcel, flags );
        parcel.writeTypedList( mAssetList );

    }

    protected PrintsPrintJob(Parcel parcel) {
        super( parcel );
        this.mAssetList = new ArrayList<Asset>();
        parcel.readTypedList( mAssetList, Asset.CREATOR);
    }

    public static final Parcelable.Creator<PrintsPrintJob> CREATOR
            = new Parcelable.Creator<PrintsPrintJob>() {
        public PrintsPrintJob createFromParcel(Parcel in) {
            return new PrintsPrintJob(in);
        }

        public PrintsPrintJob[] newArray(int size) {
            return new PrintsPrintJob[size];
        }
    };

}
